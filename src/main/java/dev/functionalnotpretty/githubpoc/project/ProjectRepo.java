package dev.functionalnotpretty.githubpoc.project;

public record ProjectRepo(
        String id,
        String name,
        String url,
        boolean isPrivate,
        String createdAt,
        String hookId
) {
}
