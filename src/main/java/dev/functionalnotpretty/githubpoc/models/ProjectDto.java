package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectRepo;
import dev.functionalnotpretty.githubpoc.entities.ProjectStatus;
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
    // DTO for entity -> response. expect/hope to use the ProjectDto for request and responses
    public static ProjectDto toDto(ProjectEntity p) {
        return new ProjectDto(
                p.getId(),
                p.getProjectId(),
                p.getUserId(),
                p.getProjectName(),
                p.getDescription(),
                p.getStatus(),
                p.getRepo(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    public static ProjectEntity toEntity(ProjectDto dto) {
        var entity = new ProjectEntity();
        entity.setId(dto.id());
        entity.setProjectId(dto.projectId);
        entity.setUserId(dto.userId);
        entity.setProjectName(dto.name);
        entity.setDescription(dto.description);
        entity.setStatus(dto.status);
        entity.setRepo(dto.repo);
        entity.setCreatedAt(dto.createdAt);
        entity.setUpdatedAt(dto.updatedAt);
        return entity;
    }
}
