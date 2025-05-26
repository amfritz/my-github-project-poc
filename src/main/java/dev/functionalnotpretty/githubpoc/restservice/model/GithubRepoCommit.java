package dev.functionalnotpretty.githubpoc.restservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GithubRepoCommit {

    private String sha;
    private String node_id;
    private JsonNode commit;
    private JsonNode author;
    private JsonNode committer;
    private JsonNode parents;
}
