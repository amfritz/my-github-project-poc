package dev.functionalnotpretty.githubpoc.controllers.webhooks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/webhooks/github")
public class GitHubWebHook {

    @PostMapping
    public ResponseEntity<String> doGitHubWebhook(@RequestBody String body) {
        // todo -- all of this
        // register a webhook that will parse out commit messages and add them to the project
        // maybe have a way to cache project ids and not query them
        return ResponseEntity.ok("Hello World, and " + body);
    }
}
