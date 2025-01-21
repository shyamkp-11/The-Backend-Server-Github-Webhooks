package com.shyampatel.webapp.githubplayroom.githubuser;

public record FcmEnabledRequestDto(
        String globalId,
        String deviceId,
        Boolean fcmEnabled
) {
}
