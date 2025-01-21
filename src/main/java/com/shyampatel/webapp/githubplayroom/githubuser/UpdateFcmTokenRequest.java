package com.shyampatel.webapp.githubplayroom.githubuser;

public record UpdateFcmTokenRequest(
        String globalId,
        String deviceId,
        String fcmToken
) {
}