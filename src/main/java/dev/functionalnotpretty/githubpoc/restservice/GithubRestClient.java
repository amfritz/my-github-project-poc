package dev.functionalnotpretty.githubpoc.restservice;

import dev.functionalnotpretty.githubpoc.entities.GitHubRepo;
import dev.functionalnotpretty.githubpoc.entities.GitHubRepoCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GithubRestClient {
    private final static Logger log = LoggerFactory.getLogger(GithubRestClient.class);
    private final RestClient githubRestClient;
    private final static String GITHUB_BASE_URL = "https://api.github.com";

    private final static String token = System.getenv("GITHUB_TOKEN");

    public GithubRestClient(RestClient.Builder restClientBuilder) {
        this.githubRestClient = restClientBuilder.baseUrl(GITHUB_BASE_URL)
                .defaultHeader("Authorization", "Bearer  " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("User-Agent", "amfritz")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    // curl -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/user/repos
    public List<GitHubRepo> getUserRepos() {
        log.info("getUserRepos called");
        return this.githubRestClient.get()
                .uri("/user/repos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubRepo>>() {});
    }

    // // curl -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/repos/amfritz/my-github-project-poc/commits
    //
    public List<GitHubRepoCommit> getUserRepoCommits(String userId, String repo) {
        log.info("getUserRepoCommits called");
        return this.githubRestClient.get()
                .uri("/repos/{userid}/{repo}/commits", userId, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubRepoCommit>>() {});
    }
}

