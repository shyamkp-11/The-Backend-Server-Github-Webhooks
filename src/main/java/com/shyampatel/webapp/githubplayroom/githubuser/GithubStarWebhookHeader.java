package com.shyampatel.webapp.githubplayroom.githubuser;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubStarWebhookHeader(
        @JsonProperty("X-Hub-Signature-256")
        String x_hub_signature_256
) {
};
