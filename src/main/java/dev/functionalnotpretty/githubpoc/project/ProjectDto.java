package dev.functionalnotpretty.githubpoc.project;

import jakarta.validation.constraints.NotEmpty;

public record ProjectDto(
        String id,
        String projectId,
        String userId,
        @NotEmpty(message = "The project name cannot be empty")
        String name,
        String description,
        ProjectStatus status,
        ProjectRepo repo,
        String createdAt,
        String updatedAt
) {
}
