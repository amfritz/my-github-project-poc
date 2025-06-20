package dev.functionalnotpretty.githubpoc.githubclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubCreateWebhookResponse(
        String type,
        long id,
        String name,
        GitHubWebhookConfig config,
        String[] events,
        boolean active,
        String updated_at,
        String created_at
) {
}
