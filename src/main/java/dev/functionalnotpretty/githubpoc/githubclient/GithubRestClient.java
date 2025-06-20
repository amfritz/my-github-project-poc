package dev.functionalnotpretty.githubpoc.githubclient;

import dev.functionalnotpretty.githubpoc.exceptions.GitRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GithubRestClient {
    private final static Logger log = LoggerFactory.getLogger(GithubRestClient.class);
    private final RestClient githubRestClient;

    private final static String GITHUB_BASE_URL = "https://api.github.com";
    private final static String token = System.getenv("GITHUB_TOKEN");

    public final static String GITHUB_LINK_HEADER = "Link";
    public final static String REL_NEXT = "rel=\"next\"";

    public GithubRestClient(RestClient.Builder restClientBuilder) {
        this.githubRestClient = restClientBuilder.baseUrl(GITHUB_BASE_URL)
                .defaultHeader("Authorization", "Bearer  " + token)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("User-Agent", "amfritz")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    // curl -i -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/user/repos
    public ResponseEntity<List<GitHubRepo>> getUserRepos() {
//        log.info("getUserRepos called");
        return this.githubRestClient
                .get()
                .uri("/user/repos")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

    // // curl -i -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/repos/amfritz/my-github-project-poc/commits
    //
    public ResponseEntity<List<GitHubRepoCommit>>  getRepoCommitsByUser(String userId, String repo) {
        String uri = "/repos/{userid}/{repo}/commits";
        return this.githubRestClient.get()
                .uri(uri, userId, repo)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

    public ResponseEntity<List<GitHubRepoCommit>>  getRepoCommitsPaginated(String uri) {
        return this.githubRestClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
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
                    if (resp.getStatusCode().isError()) {
                        var body = new String(resp.getBody().readAllBytes());
                        log.error("createGitHubRepositoryWebHook client error response {} ", body);
                        throw new GitRequestException("createGitHubRepositoryWebHook response " + body, resp.getStatusCode().value());
                    }
                    return resp.bodyTo(GitHubCreateWebhookResponse.class);
                });
    }

    public void deleteGitHubWebhook(String userId, String repository, String webhookId) {
            log.info("GitHubDeleteWebhook called");
            this.githubRestClient.delete()
                    .uri("/repos/{userid}/{repository}/hooks/{webhookId}", userId, repository, webhookId)
                    .retrieve()
                    .toBodilessEntity();
    }

}

