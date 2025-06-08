package dev.functionalnotpretty.githubpoc.restservice;

import dev.functionalnotpretty.githubpoc.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
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

    // curl -i -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/user/repos
    public List<GitHubRepo> getUserRepos() {
        log.info("getUserRepos called");
        return this.githubRestClient.get()
                .uri("/user/repos")
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubRepo>>() {});
    }

    // // curl -i -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/repos/amfritz/my-github-project-poc/commits
    //
    public List<GitHubRepoCommit> getUserRepoCommits(String userId, String repo) {
        log.info("getUserRepoCommits called");
        return this.githubRestClient.get()
                .uri("/repos/{userid}/{repo}/commits", userId, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubRepoCommit>>() {});
    }

    public GitHubCreateWebhookResponse createGitHubRepositoryWebHook(String userId, String repository) {
        log.info("createGitHubRepositoryWebHook called");
        var request = new GitHubCreateWebhookRequest(
                "web", new GitHubWebhookConfig(System.getenv("GITHUB_WEBHOOK_URL"), "json", System.getenv("GITHUB_SECRET"),"1"),
                new String[] {"push"}, true);
        return this.githubRestClient.post()
                .uri("/repos/{userId}/{repository}/hooks", userId, repository)
                .body(request)
                .exchange( (req, resp) -> {
                    if (resp.getStatusCode().is4xxClientError() || resp.getStatusCode().is5xxServerError()) {
                        log.error("createGitHubRepositoryWebHook returned HTTP error code {} : {}",resp.getStatusCode(), resp);
                        // todo -- handle errors better
                        throw new RuntimeException("createGitHubRepositoryWebHook returned HTTP error code " + resp.getStatusCode());
                    } else {
                        return resp.bodyTo(GitHubCreateWebhookResponse.class);
                    }
                });
    }

    public boolean deleteGitHubWebhook(String userId, String repository, String webhookId) {
        log.info("GitHubDeleteWebhook called");
        var response = this.githubRestClient.delete()
                .uri("/repos/{userid}/{repository}/hooks/{webhookId}", userId, repository, webhookId)
                .retrieve()
                .toBodilessEntity();

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            log.error("unable to delete webhook, returned HTTP error code {} : {}",response.getStatusCode(), response);
            return false;
        }
        return true;
    }
}

