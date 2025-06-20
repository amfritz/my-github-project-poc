package dev.functionalnotpretty.githubpoc.restservice;

import dev.functionalnotpretty.githubpoc.entities.*;
import dev.functionalnotpretty.githubpoc.exceptions.GitRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubRestClient {
    private final static Logger log = LoggerFactory.getLogger(GithubRestClient.class);
    private final RestClient githubRestClient;
    private final static String GITHUB_BASE_URL = "https://api.github.com";

    private final static String token = System.getenv("GITHUB_TOKEN");

    public final static String GITHUB_LINK_HEADER = "Link";
    private final static String REL_NEXT = "rel=\"next\"";

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
                .body(new ParameterizedTypeReference<>() {
                });
    }

    // // curl -i -L -H "Accept: application/vnd.github+json" -H "Authorization: Bearer " -H "X-GitHub-Api-Version: 2022-11-28"  https://api.github.com/repos/amfritz/my-github-project-poc/commits
    //
    public List<GitHubRepoCommit> getUserRepoCommits(String userId, String repo) {
        log.info("getUserRepoCommits called");

        var result = new ArrayList<GitHubRepoCommit>();
        String uri = "/repos/{userid}/{repo}/commits";

        do {
            ResponseEntity<List<GitHubRepoCommit>> resp = this.githubRestClient.get()
                    .uri(uri, userId, repo)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {
                    });

            var commits = resp.getBody();
            result.addAll(commits);

            // check headers to see if there's a link for more commits. if so, iterate though the pages
            if (resp.getHeaders().containsKey(GITHUB_LINK_HEADER)) {
                String next = null;
                String[] pageLinks = StringUtils.split(resp.getHeaders().get(GITHUB_LINK_HEADER).getFirst(), ',');
                for (String s : pageLinks) {
                    // the links could have rel="first", rel="prev", rel="next", rel="last" sent. only get the next one.
                    // if no next then we are at the end and break out.
                    String[] split = StringUtils.split(s, ';');
                    if (StringUtils.trim(split[1]).equalsIgnoreCase(REL_NEXT)) {
                        log.info("next link found: {}", split[0]);
                        next = split[0];
                    }
                }
                if (next ==  null) {
                    log.info("next link not found, breaking out");
                    break;
                }
                // the url is bracketed by < and > so strip them off
                uri = StringUtils.strip( next, "<>");
                log.info("getUserRepoCommits link [{}]", uri);
            } else {
                // no link header in response
                break;
            }
        }
        while(true);

        return result;
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

