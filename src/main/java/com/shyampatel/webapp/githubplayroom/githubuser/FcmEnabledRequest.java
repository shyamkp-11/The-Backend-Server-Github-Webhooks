package com.shyampatel.webapp.githubplayroom.githubuser;

public record FcmEnabledRequest(
        String globalId,
        String deviceId,
        Boolean fcmEnabled
) {
}