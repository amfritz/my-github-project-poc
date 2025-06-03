package dev.functionalnotpretty.githubpoc.models;

import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

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
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        String eventDate,
        String repoName,
        String branchName,
        boolean isNewEvent,
        String createdDt,
        String updatedDt
        ) {

        public static ProjectEvent mapDto(ProjectEventDto dto) {
                return new ProjectEvent(dto);
        }

        public static ProjectEventDto toDto(ProjectEvent e) {
                return new ProjectEventDto(
                        e.getId(),
                        e.getUserId(),
                        e.getProjectId(),
                        e.getEventDescription(),
                        e.getEventDate(),
                        e.getRepoName(),
                        e.getBranch_name(),
                        e.isNewEvent(),
                        e.getCreatedDt(),
                        e.getUpdatedDt()
                );
        }
}
