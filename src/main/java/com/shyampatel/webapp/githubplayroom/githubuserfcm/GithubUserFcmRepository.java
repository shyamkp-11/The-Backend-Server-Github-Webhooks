package com.shyampatel.webapp.githubplayroom.githubuserfcm;

import com.shyampatel.webapp.githubplayroom.githubuser.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GithubUserFcmRepository extends JpaRepository<GithubUserFcm, Long> {

    List<GithubUserFcm> getGithubUserFcmByGithubUser(GithubUser githubUser);

    GithubUserFcm getGithubUserFcmByGithubUserAndDeviceId(GithubUser githubUser, String deviceId);

    List<GithubUserFcm> getGithubUserFcmsByGithubUser_GlobalIdAndEnabledAndSignedOut(String githubUserGlobalId, Boolean enabled, Boolean signedOut);

    List<GithubUserFcm> getGithubUserFcmsByFcmToken(String fcmToken);
}