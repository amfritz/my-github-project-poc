package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import jakarta.validation.constraints.NotEmpty;

public record ProjectEventDto(
        String id,
        // user id is for poc, would grab the user id from authenticated user
        @NotEmpty(message = "the user ID cannot be empty")
        String userId,
        @NotEmpty(message = "The project ID cannot be empty")
        String projectId,
        @NotEmpty(message = "The event description cannot be empty")
        String eventDescription,
        @NotEmpty(message = "The event date cannot be empty")
        String eventDate,
        String repoName,
        String branchName,
        boolean isNewEvent,
        String createdDt,
        String updatedDt
        ) {
}
