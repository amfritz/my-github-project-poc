package dev.functionalnotpretty.githubpoc.projectevents;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
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
    private boolean isNewEvent;
    private String createdDt;       // todo -- this isn't going to work like i think it will work. ne
    private String updatedDt;

    public ProjectEvent() {
    }

    public ProjectEvent(String userId, String projectId, String eventDescription, String eventDate, boolean isNewEvent,
                        String createdDt, String updatedDt) {
        this.userId = userId;
        this.projectId = projectId;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.isNewEvent = isNewEvent;
        this.createdDt = createdDt;
        this.updatedDt = updatedDt;
    }
}
