package dev.functionalnotpretty.githubpoc.controllers.webhooks;

import dev.functionalnotpretty.githubpoc.controllers.ProjectsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/webhooks/github")
public class GitHubWebHook {
    private final static Logger log = LoggerFactory.getLogger(GitHubWebHook.class);

    @PostMapping
    public ResponseEntity<String> doGitHubWebhook(@RequestBody String body) {
       // todo -- all of this
        // register a webhook that will parse out commit messages and add them to the project
        // maybe have a way to cache project ids and not query them
        log.info("received request: {}", body);
        return ResponseEntity.ok("Hello World, and " + body);
    }
}
