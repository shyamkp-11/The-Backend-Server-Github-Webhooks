package com.shyampatel.webapp.githubplayroom.githubuser;

public record GithubUserResponseDto(
        String globalId,
        String deviceId,
        Boolean fcmEnabled
) {
}
