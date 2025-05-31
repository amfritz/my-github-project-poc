package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectRepo;

public record ProjectDto(
        String id,
        String userId,
        String projectName,
        String description,
        ProjectRepo repo,
        String createdAt,
        String updatedAt
) {
    // DTO for entity -> response. expect/hope to use the ProjectDto for request and responses
    public static ProjectDto mapToDto(ProjectEntity p) {
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
