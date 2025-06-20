package dev.functionalnotpretty.githubpoc.githubclient;

public record GitHubWebhookConfig(
        String url,
        String content_type,
        String secret,
        String insecure_ssl
) {
}
