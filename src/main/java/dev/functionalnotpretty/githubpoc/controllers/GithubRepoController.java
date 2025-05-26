package dev.functionalnotpretty.githubpoc.controllers;

import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepo;
import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepoCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/repos")
public class GithubRepoController {
    private final static Logger log = LoggerFactory.getLogger(GithubRepoController.class);
    private final GithubRestClient githubRestClient;

    public GithubRepoController(final GithubRestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    @GetMapping
    public List<GithubRepo> getRepos() {
        log.info("get repos");
        var result = githubRestClient.getUserRepos();
        log.info("get {} repos", result.size());
        // TODO -- dto mapping ?
        // TODO -- handle errors & no content. this is POC.
        return result;
    }

    @GetMapping("{repo}/commits")
    public List<GithubRepoCommit> getRepoCommits(@PathVariable("repo") String repo) {
        log.info("get repo ({}) commits", repo);
        var commits = githubRestClient.getUserRepoCommits("amfritz", repo);
        log.info("got {} commits ", commits.size());
        // TODO -- clean this up
        return commits;
    }
}
