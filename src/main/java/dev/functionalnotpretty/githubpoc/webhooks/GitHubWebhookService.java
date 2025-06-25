package dev.functionalnotpretty.githubpoc.webhooks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.functionalnotpretty.githubpoc.projectevents.ProjectEvent;
import dev.functionalnotpretty.githubpoc.projectevents.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.project.ProjectRepository;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.ArrayList;

@Service
public class GitHubWebhookService {
    private final static Logger log = LoggerFactory.getLogger(GitHubWebhookService.class);
    private final ProjectRepository projectRepository;
    private final ProjectEventsRepository eventsRepository;
    private final String gitHubSecret;

    public GitHubWebhookService(ProjectRepository projectRepository, ProjectEventsRepository eventsRepository) {
        this.projectRepository = projectRepository;
        this.eventsRepository = eventsRepository;
        this.gitHubSecret = System.getenv("GITHUB_SECRET");
    }

    public void handleWebhookRequest(HttpHeaders headers, String body) {
        var headerVal = headers.get("X-GitHub-Hook-ID");
        var hookId = headerVal !=null ? headerVal.getFirst(): "";
        headerVal = headers.get("X-GitHub-Event");
        var request = headerVal!=null ? headerVal.getFirst(): "";

        log.info("received webhook event({})", hookId);

        headerVal = headers.get("x-hub-signature-256");
        var signature = headerVal!=null ? headerVal.getFirst(): "";
        if (StringUtils.isEmpty(signature)) {
            log.error("signature is empty!");
            return;
        }
        if (!this.validateRequest(body, signature)) {
            log.error("unable to validate the request!");
            return;
        }

        if (StringUtils.equalsIgnoreCase(request, "ping")){
            log.info("github ping event received.");
        }
        if (StringUtils.equalsIgnoreCase(request, "push")){
            log.info("github push event received. body: {}", body);
            this.processPushEvent(body);
        }

    }

    private boolean validateRequest(String body, String signature) {
        try {
            var sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(this.gitHubSecret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);

            byte[] hash = sha256_HMAC.doFinal(body.getBytes());
            String message =  "sha256=" + Hex.encodeHexString(hash);

            return message.equals(signature);
        } catch (Exception e) {
            log.error("Error while validating request: {}", e.getMessage());
            return false;
        }
    }

    private void processPushEvent(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode pushEvent = mapper.readTree(body);
            var repositoryName = pushEvent.get("repository").get("name").asText();
            var project = this.projectRepository.findByRepo_Name(repositoryName);

            log.info("found project {}, creating events for it", project);

            JsonNode commits = pushEvent.get("commits");
            log.info("read {} commit messages from webhook event", commits.size());
            var events = new ArrayList<ProjectEvent>();
            for (int j = 0; j < commits.size(); j++) {
                var commit = commits.get(j);
                var created = Instant.now().toString();
                var event = new ProjectEvent("amfritz", project.getProjectId(),
                        commit.get("message").asText(),
                        commit.get("timestamp").asText(),
                         true, created, created);
                events.add(event);
            }

            log.info("created events for project {}, found events {}:{}", project, events.size(), events);
            this.eventsRepository.saveAll(events);
        }
        catch (Exception e) {
        log.error("Error while processing push event: {}", e.getMessage());
        }
    }

}
