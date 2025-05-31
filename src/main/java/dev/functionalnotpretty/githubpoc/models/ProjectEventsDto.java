package dev.functionalnotpretty.githubpoc.models;

public record ProjectEventsDto(
        String id,
        String userId,
        String projectId,
        String eventDescription,
        String eventDate,
        String repoName,
        String branchName,
        boolean isNewEvent
        ) {
}
