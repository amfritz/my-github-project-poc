package dev.functionalnotpretty.githubpoc.entities;

public record GitHubCreateWebhookRequest(
        String name,
        GitHubWebhookConfig config,
        String[] events,
        boolean active
) {}
