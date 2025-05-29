package dev.functionalnotpretty.githubpoc.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitHubRepoCommit {

    private String sha;
    private String node_id;
    private JsonNode commit;
    private JsonNode parents;
}
