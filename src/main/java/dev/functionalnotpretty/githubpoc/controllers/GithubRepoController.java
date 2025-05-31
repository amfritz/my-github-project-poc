package dev.functionalnotpretty.githubpoc.controllers;

import dev.functionalnotpretty.githubpoc.entities.GitHubRepo;
import dev.functionalnotpretty.githubpoc.entities.GitHubRepoCommit;
import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/repos")
public class GithubRepoController {
    private final static Logger log = LoggerFactory.getLogger(GithubRepoController.class);
    private final GithubRestClient githubRestClient;

    public GithubRepoController(final GithubRestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    @GetMapping
    public List<GitHubRepo> getRepos() {
        log.info("get repos");
        // returning the 'raw' github object to the client. it's read only, client doesn't operate on gh data.
        var result = githubRestClient.getUserRepos();
        log.info("get {} repos", result.size());
        // TODO -- handle errors & no content. this is POC.
        return result;
    }

    @GetMapping("{repo}/commits")
    public List<GitHubRepoCommit> getRepoCommits(@PathVariable("repo") String repo) {
        log.info("getting repo ({}) commits", repo);
        // returning the 'raw' github object to the client. it's read only, client doesn't operate on gh data.
        var commits = githubRestClient.getUserRepoCommits("amfritz", repo);
        log.info("got {} commits ", commits.size());
        return commits;
    }
}
