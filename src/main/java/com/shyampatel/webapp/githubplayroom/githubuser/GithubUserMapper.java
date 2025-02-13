package com.shyampatel.webapp.githubplayroom.githubuser;

import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcm;
import org.springframework.stereotype.Service;

@Service
public class GithubUserMapper {
    public GithubUser togithubUser(GithubUserDto githubUserDto) {
        return GithubUser.builder()
                .setGlobalId(githubUserDto.globalId())
                .setFirstName(githubUserDto.firstName())
                .setLastName(githubUserDto.lastName())
                .setEmail(githubUserDto.email())
                .setUsername(githubUserDto.username())
                .build();
    }

    public GithubUserResponseDto togithubUserResponseDto(GithubUser githubUser, GithubUserFcm githubUserFcm) {
        return new GithubUserResponseDto(
                githubUser.getGlobalId(),
                githubUserFcm.getDeviceId(),
                githubUserFcm.getEnabled());
    }
}
