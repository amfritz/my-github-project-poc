package dev.functionalnotpretty.githubpoc.repo;


import dev.functionalnotpretty.githubpoc.githubclient.GitHubRepo;
import dev.functionalnotpretty.githubpoc.githubclient.GitHubRepoCommit;
import dev.functionalnotpretty.githubpoc.githubclient.GithubRestClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepoService {
    private final static Logger log = LoggerFactory.getLogger(RepoService.class);
    private final GithubRestClient githubRestClient;

    public RepoService(GithubRestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    public List<GitHubRepo> getUserRepos() {
        log.info("get list of repos from github");
        // returning the 'raw' GitHub object to the client. it's read only, client doesn't operate on gh data.
        var result = githubRestClient.getUserRepos().getBody();
        log.info("read {} repos", result!= null ? result.size() : "null response");
        //  handle errors & no content. this is POC.
        return result;
    }

    public List<GitHubRepoCommit> getRepoCommits(String userId, String repoName) {
        log.info("getUserRepoCommits called, userId={}, repoName={}", userId, repoName);

        ResponseEntity<List<GitHubRepoCommit>> resp = this.githubRestClient.getRepoCommitsByUser(userId,repoName);
        var commits = resp.getBody();
        if (commits == null || commits.isEmpty()) {
            return new ArrayList<>();
        }

        log.info("read {} commits", commits.size());
        var result = new ArrayList<>(commits);

            // check headers to see if there's a link for more commits. if so, iterate though the pages
        var linkHeader = resp.getHeaders().get(GithubRestClient.GITHUB_LINK_HEADER);
        if ((linkHeader != null) && (!linkHeader.isEmpty())) {
            boolean next = true;
            do {
                log.info("link header: {}", linkHeader);
                String[] pageLinks = StringUtils.split(linkHeader.getFirst(), ',');
                for (String s : pageLinks) {
                    log.info("link: {}", s);
                    // the links could have rel="first", rel="prev", rel="next", rel="last" sent. only get the next one.
                    // if no next then we are at the end and break out.
                    String[] split = StringUtils.split(s, ';');
                    if (StringUtils.trim(split[1]).equalsIgnoreCase(GithubRestClient.REL_NEXT)) {
                        // the url is bracketed by < and > so strip them off
                        String uri = StringUtils.strip( split[0], "<>");
                        log.info("getUserRepoCommits next pagination link [{}]", uri);
                        ResponseEntity<List<GitHubRepoCommit>> page = this.githubRestClient.getRepoCommitsPaginated(uri);
                         var pageCommits = page.getBody();
                         log.info("read {} commits", pageCommits == null? 0 : pageCommits.size());
                        if (pageCommits != null && !pageCommits.isEmpty()) {
                            result.addAll(pageCommits);
                        }
                        linkHeader = page.getHeaders().get(GithubRestClient.GITHUB_LINK_HEADER);
                        break;
                    }
                    else {
                        next = false;
                        log.info("next link not found ending the loop");
                    }
                }
            }
            while(next);
        }

        return result;
    }
}
