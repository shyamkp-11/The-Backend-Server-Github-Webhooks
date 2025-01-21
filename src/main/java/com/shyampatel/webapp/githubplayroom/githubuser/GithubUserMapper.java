package com.shyampatel.webapp.githubplayroom.githubuser;

import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcm;
import org.springframework.stereotype.Service;

@Service
public class GithubUserMapper {
    public GithubUser togithubUser(GithubUserDto githubUserDto) {
        return GithubUser.builder()
                .globalId(githubUserDto.globalId())
                .firstName(githubUserDto.firstName())
                .lastName(githubUserDto.lastName())
                .email(githubUserDto.email())
                .username(githubUserDto.username())
                .build();
    }

    public GithubUserResponseDto togithubUserResponseDto(GithubUser githubUser, GithubUserFcm githubUserFcm) {
        return new GithubUserResponseDto(
                githubUser.getGlobalId(),
                githubUserFcm.getDeviceId(),
                githubUserFcm.getEnabled());
    }
}
