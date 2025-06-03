package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Container(containerName = "projects", autoCreateContainer = false)
public class ProjectEntity {
    // key / primary elements
    private String id;      //  project id
    @PartitionKey
    private String projectId;       // copy of the id, cannot be system generated
    private String userId;

    // this may not scale, in theory, and may need to add a trigger to create a user container for the user info and their
    // created projects

    // rest of the payload
    private String projectName;
    private String description;
    private ProjectRepo repo;
    private String createdAt;
    private String updatedAt;

    public ProjectEntity() {
    }

    public ProjectEntity(ProjectDto project) {
        this.id = project.id();
        this.projectId = project.id();
        this.userId = project.userId();
        this.projectName = project.name();
        this.description = project.description();
        this.repo = project.repo();     // i wonder if this is correct
        this.createdAt = project.createdAt();
        this.updatedAt = project.updatedAt();
    }

    public ProjectEntity(String userId, String projectName, String description, String repoName, String repoId, String repoUrl, boolean repoIsPrivate, String repoCreateDt, String createdAt, String updatedAt) {
        this.userId = userId;
        this.projectName = projectName;
        this.description = description;
        this.repo = new ProjectRepo(repoId, repoName, repoUrl, repoIsPrivate,repoCreateDt);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            return String.format("[%s]\tItem:\t%s", this.id, this.projectName);
        }
    }
}