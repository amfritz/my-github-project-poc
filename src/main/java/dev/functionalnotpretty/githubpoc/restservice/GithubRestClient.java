package dev.functionalnotpretty.githubpoc.restservice;

import dev.functionalnotpretty.githubpoc.entities.*;
import dev.functionalnotpretty.githubpoc.exceptions.GitRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
//                .defaultStatusHandler(HttpStatusCode::isError,  (req, resp) -> {
//                    log.error("default github error handler, returned HTTP error code {} : {}",resp.getStatusCode(),resp.getBody());
//                    throw new RuntimeException("a github error has occurred: " + resp.getStatusCode());
//                })
//                .requestInterceptor((request, body, execution) -> {
//                    logRequest(request, body);
//                    var response = execution.execute(request, body);
//                    logResponse(request, response);
//                    return response;
//                })
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

//    private void logRequest(HttpRequest request, byte[] body) {
//        log.info("Request: {} {}", request.getMethod(), request.getURI());
//        //logHeaders(request.getHeaders());
//        for(var header : request.getHeaders().entrySet()) {
//            log.info("Response header: {} {}", header.getKey(), header.getValue());
//        }
//        if (body != null && body.length > 0) {
//            log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
//        }
//    }
//
//    private void logResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
//        log.info("Response status: {}", response.getStatusCode());
//        //logHeaders(response.getHeaders());
//        for(var header : response.getHeaders().entrySet()) {
//            log.info("Response header: {} {}", header.getKey(), header.getValue());
//        }
//        byte[] responseBody = response.getBody().readAllBytes();
//        if (responseBody.length > 0) {
//            log.info("Response body: {}", new String(responseBody, StandardCharsets.UTF_8));
//        }
//    }

}

