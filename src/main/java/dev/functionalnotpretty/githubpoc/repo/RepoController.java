package dev.functionalnotpretty.githubpoc.repo;

import dev.functionalnotpretty.githubpoc.githubclient.GitHubRepo;
import dev.functionalnotpretty.githubpoc.githubclient.GitHubRepoCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/repos")
public class RepoController {
    private final static Logger log = LoggerFactory.getLogger(RepoController.class);
    private final RepoService repoService;

    public RepoController(final RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping
    public List<GitHubRepo> getRepos() {
        log.info("get list of repos from github");
        // This would need to be refactored to pass in a user token for different users
        return repoService.getUserRepos();
    }

    @GetMapping("{repo}/commits")
    public List<GitHubRepoCommit> getRepoCommits(@PathVariable("repo") String repo) {
        log.info("getting repo ({}) commits", repo);
        // returning the 'raw' github object to the client. it's read only, client doesn't operate on gh data.
        return this.repoService.getRepoCommits("amfritz", repo);
    }
}
