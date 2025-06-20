package dev.functionalnotpretty.githubpoc.project;

import dev.functionalnotpretty.githubpoc.projectevents.ProjectEventDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProjectDto(
        @NotEmpty(message = "The project name cannot be empty")
        String name,
        String description,
        ProjectRepo repo,
        List<ProjectEventDto> events
) {
}
