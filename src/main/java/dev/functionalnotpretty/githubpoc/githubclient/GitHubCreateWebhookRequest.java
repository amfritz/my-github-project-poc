package dev.functionalnotpretty.githubpoc.githubclient;

public record GitHubCreateWebhookRequest(
        String name,
        GitHubWebhookConfig config,
        String[] events,
        boolean active
) {}
