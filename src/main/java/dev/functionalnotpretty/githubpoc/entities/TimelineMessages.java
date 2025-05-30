package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Container(containerName = "projects", autoCreateContainer = false)
public class TimelineMessages {
    private final String type = "messages";
    private String sha;
    @Id
    private String node_id;
    @PartitionKey
    private String userId;
    private String name;
    private String author_name;
    private String author_email;
    private String branch_name;
    private String message;

    public TimelineMessages() {
    }

    public TimelineMessages(String sha, String node_id, String name, String userId, String author_name, String author_email, String branch_name, String message) {
        this.sha = sha;
        this.node_id = node_id;
        this.name = name;
        this.userId = userId;
        this.author_name = author_name;
        this.author_email = author_email;
        this.branch_name = branch_name;
        this.message = message;
    }
}
