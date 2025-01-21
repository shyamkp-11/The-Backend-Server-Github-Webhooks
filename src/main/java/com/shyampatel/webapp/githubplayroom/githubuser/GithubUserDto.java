package com.shyampatel.webapp.githubplayroom.githubuser;


import jakarta.validation.constraints.NotBlank;

public record GithubUserDto(
        String firstName,
        String lastName,
        String username,
        String email,
        @NotBlank
        String globalId,
        @NotBlank
        String deviceId,
        @NotBlank
        String fcmToken) {
}
