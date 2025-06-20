package dev.functionalnotpretty.githubpoc.webhooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.trace("doGitHubWebhook called");
        this.gitHubWebhookService.handleWebhookRequest(headers, body);
        return ResponseEntity.accepted().body("");
    }
}
