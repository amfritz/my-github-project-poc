package dev.functionalnotpretty.githubpoc.githubclient;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class GitHubRepo {
    private String id;
    private String node_id;
    private String name;
    private String full_name;
    @JsonAlias("private")
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private String html_url;
    private String description;
}
