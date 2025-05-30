package dev.functionalnotpretty.githubpoc.models;

import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import dev.functionalnotpretty.githubpoc.entities.WatchedRepo;

public record WatchedRepoDto(
        String id,
        String name,
        String userId,
        String full_name,
        boolean isPrivate,
        String html_url,
        String description
) {
    public static WatchedRepoDto mapToDto(WatchedRepo repo) {
        return new WatchedRepoDto(
                repo.getId(),
                repo.getName(),
                repo.getUserId(),
                repo.getFull_name(),
                repo.isPrivate(),
                repo.getHtml_url(),
                repo.getDescription()
        );
    }
}
