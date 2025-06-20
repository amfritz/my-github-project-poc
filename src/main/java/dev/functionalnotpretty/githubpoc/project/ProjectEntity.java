package dev.functionalnotpretty.githubpoc.project;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ProjectStatus status;          // active / archived (no webhook)
    private ProjectRepo repo;
    // todo -- consider the spring annotations for auditing, but seems must be date objects and not strings
    private String createdAt;
    private String updatedAt;

    public ProjectEntity() {
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