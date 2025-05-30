package dev.functionalnotpretty.githubpoc.entities;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.functionalnotpretty.githubpoc.models.WatchedRepoDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Container(containerName = "projects", autoCreateContainer = false)
public class WatchedRepo {
    private final String type = "watchedRepo";
    private String id;
    private String name;
    @PartitionKey
    private String userId;
    private String full_name;
    private boolean isPrivate;
    private String html_url;
    private String description;

    public WatchedRepo() {
    }

    public WatchedRepo(WatchedRepoDto watchedRepo) {
        this.id = watchedRepo.id();
        this.name = watchedRepo.name();
        this.userId = watchedRepo.userId();
        this.full_name = watchedRepo.full_name();
        this.isPrivate = watchedRepo.isPrivate();
        this.html_url = watchedRepo.html_url();
        this.description = watchedRepo.description();
    }

    public WatchedRepo(String id, String name, String userId, String node_id, String full_name, boolean isPrivate, String html_url, String description) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.full_name = full_name;
        this.isPrivate = isPrivate;
        this.html_url = html_url;
        this.description = description;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            return String.format("[%s]\tItem:\t%s", this.id, this.name);
        }
    }
}