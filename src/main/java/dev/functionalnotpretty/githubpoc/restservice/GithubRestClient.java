package dev.functionalnotpretty.githubpoc.restservice;

import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepo;
import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepoCommit;
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

    public List<GithubRepo> getUserRepos() {
        log.info("getUserRepos called");
        try {
            var result = this.githubRestClient.get()
                    .uri("/user/repos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GithubRepo>>() {});
//            if (result!= null) {
//                log.info("got {} repos", result.size());
//            }
            return result;
        }
        catch (Exception e) {
            log.error("Exception caught during github request", e);
        }
        return null;
    }

    public List<GithubRepoCommit> getUserRepoCommits(String userId, String repo) {
        log.info("getUserRepoCommits called");
        try {
            var result = this.githubRestClient.get()
                    .uri("/repos/{userid}/{repo}/commits", userId, repo)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GithubRepoCommit>>() {});
//            if (result!= null) {
//                log.info("got {} commits", result.size());
//            }
            return result;
        }
        catch (Exception e) {
            log.error("Exception caught during github request", e);
        }
        return null;
    }


}
