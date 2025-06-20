package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectRepo;
import dev.functionalnotpretty.githubpoc.entities.ProjectStatus;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProjectDto(
        String userId,
        @NotEmpty(message = "The project name cannot be empty")
        String name,
        String description,
        ProjectRepo repo,
        List<ProjectEventDto> events
) {
}
