package dev.functionalnotpretty.githubpoc.projectevents;

import jakarta.validation.constraints.NotEmpty;

public record ProjectEventDto(
        String id,
        // user id is for poc, would grab the user id from authenticated user
        @NotEmpty(message = "The project ID cannot be empty")
        String projectId,
        @NotEmpty(message = "The event description cannot be empty")
        String eventDescription,
        @NotEmpty(message = "The event date cannot be empty")
        String eventDate,
        boolean isNewEvent,
        String createdDt,
        String updatedDt
        ) {
}
