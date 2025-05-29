package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.GitHubRepo;

public record GitHubRepoResponse(
    String id,
    String name,
    String full_name,
    String description,
    String html_url,
    Boolean isPrivate
) {
    public static GitHubRepoResponse mapRepo(GitHubRepo repo) {
        return new GitHubRepoResponse(
                repo.getId(),
                repo.getName(),
                repo.getFull_name(),
                repo.getDescription(),
                repo.getHtml_url(),
                repo.isPrivate()
        );
    }
}
