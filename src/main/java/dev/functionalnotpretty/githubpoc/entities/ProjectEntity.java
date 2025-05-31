package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Container(containerName = "projects", autoCreateContainer = false)
public class ProjectEntity {
    // key / primary elements
    @Id
    @GeneratedValue
    private String id;      // system generated. project id
    private final String type = "project";      // private to entity
    @PartitionKey
    private String userId;

    // rest of the payload
    private String projectName;
    private String description;
    private ProjectRepo repo;
    private String createdAt;
    private String updatedAt;

    // todo -- i wonder if this will be written to the db. we don't want it written
    public static final String PROJECT_TYPE = "project";

    public ProjectEntity() {
    }

    public ProjectEntity(ProjectDto project) {
        this.id = project.id();
        this.userId = project.userId();
        this.projectName = project.projectName();
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