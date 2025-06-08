package dev.functionalnotpretty.githubpoc.controllers.webhooks;

import com.azure.core.annotation.Headers;
import dev.functionalnotpretty.githubpoc.controllers.ProjectsController;
import dev.functionalnotpretty.githubpoc.services.GitHubWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/webhooks/github")
public class GitHubWebHook {
    private final static Logger log = LoggerFactory.getLogger(GitHubWebHook.class);
    private final GitHubWebhookService gitHubWebhookService;

    public GitHubWebHook(GitHubWebhookService gitHubWebhookService) {
        this.gitHubWebhookService = gitHubWebhookService;
    }

    @PostMapping
    public ResponseEntity<String> doGitHubWebhook(@RequestBody String body, @RequestHeader HttpHeaders headers) {
        log.info("received request: {}", body);
//        for(var  entry : headers.entrySet()) {
//            log.info("header, {}: {}", entry.getKey(), entry.getValue());
//        }
        this.gitHubWebhookService.handleWebhookRequest(headers, body);
        return ResponseEntity.accepted().body("");
    }
}
