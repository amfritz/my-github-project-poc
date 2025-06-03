package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import dev.functionalnotpretty.githubpoc.models.ProjectEventDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Container(containerName = "events", autoCreateContainer = false)
public class ProjectEvent {
    @Id
    @GeneratedValue
    private String id;      // system generated. project id
    @PartitionKey
    private String projectId;
    private String userId;
    private String eventDescription;
    private String eventDate;
    private String repoName;
    private String branch_name;
    private boolean isNewEvent;
    private String createdDt;       // todo -- this isn't going to work like i think it will work. ne
    private String updatedDt;

    private final static String EVENT_TYPE = "event";

    public ProjectEvent() {
    }

    public ProjectEvent(ProjectEventDto dto) {
        this.id = dto.id();
        this.projectId = dto.projectId();
        this.userId = dto.userId();
        this.eventDescription = dto.eventDescription();
        this.eventDate = dto.eventDate();
        this.repoName = dto.repoName();
        this.branch_name = dto.branchName();
        this.isNewEvent = dto.isNewEvent();
        this.createdDt = dto.createdDt();
        this.updatedDt = dto.updatedDt();
    }

    public ProjectEvent(String userId, String projectId, String eventDescription, String eventDate, String repoName, String branch_name, boolean isNewEvent,
                        String createdDt, String updatedDt) {
        this.userId = userId;
        this.projectId = projectId;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.repoName = repoName;
        this.branch_name = branch_name;
        this.isNewEvent = isNewEvent;
        this.createdDt = createdDt;
        this.updatedDt = updatedDt;
    }
}
