package dev.functionalnotpretty.githubpoc.project;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProjectStatus {
    @JsonProperty("active")
    ACTIVE("active"),
    @JsonProperty("archived")
    ARCHIVED("archived");
    private final String value;
    ProjectStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
