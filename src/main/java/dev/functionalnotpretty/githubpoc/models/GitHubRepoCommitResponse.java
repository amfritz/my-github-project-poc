package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.GitHubRepoCommit;

public record GitHubRepoCommitResponse(
        String sha,
        String node_id,
        String date,
        String author_name,
        String author_email,
        String branch_name,
        String message

) {
    public static GitHubRepoCommitResponse mapCommit(GitHubRepoCommit commit) {
        // todo -- this doesn't handle any errors
        // todo -- the branch name will most likely be deleted. need to use a webhook to listen and save them and match up later
        return new GitHubRepoCommitResponse(
                commit.getSha(),
                commit.getNode_id(),
                commit.getCommit().path("author").path("date").asText(),
                commit.getCommit().path("author").path("name").asText(),
                commit.getCommit().path("author").path("email").asText(),
                "",
                commit.getCommit().path("message").asText()
        );
    }
}
