package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Container(containerName = "projects", autoCreateContainer = false)
public class ProjectEvents {
    @Id
    @GeneratedValue
    private String id;      // system generated. project id
    private final String type = "event";      // private to entity
    @PartitionKey
    private String userId;

    private String projectId;
    private String eventDescription;
    private String eventDate;
    private String repoName;
    private String branch_name;
    private boolean isNewEvent;

    private final static String EVENT_TYPE = "event";

    public ProjectEvents() {
    }

    public ProjectEvents(String userId, String projectId, String eventDescription, String eventDate, String repoName, String branch_name, boolean isNewEvent) {
        this.userId = userId;
        this.projectId = projectId;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.repoName = repoName;
        this.branch_name = branch_name;
        this.isNewEvent = isNewEvent;
    }
}
