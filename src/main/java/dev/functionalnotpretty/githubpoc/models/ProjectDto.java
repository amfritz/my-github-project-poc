package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectRepo;
import jakarta.validation.constraints.NotEmpty;

public record ProjectDto(
        String id,
        String userId,
        @NotEmpty(message = "The project name cannot be empty")
        String name,
        String description,
        ProjectRepo repo,
        String createdAt,
        String updatedAt
) {
    // DTO for entity -> response. expect/hope to use the ProjectDto for request and responses
    public static ProjectDto toDto(ProjectEntity p) {
        return new ProjectDto(
                p.getId(),
                p.getUserId(),
                p.getProjectName(),
                p.getDescription(),
                p.getRepo(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
