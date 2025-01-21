package com.shyampatel.webapp.githubplayroom.githubuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyampatel.webapp.githubplayroom.HmacUtils;
import com.shyampatel.webapp.githubplayroom.config.GithubServerProperties;
import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcm;
import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GithubUserService {

    private final GithubUserRepository repository;
    private final GithubUserFcmRepository githubUserFcmRepository;
    private final GithubUserMapper githubUserMapper;
    private final GithubServerProperties githubServerProperties;
    private final WebClient githubWebClient;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private final FcmService fcmService;

    @Transactional
    public GithubUserResponseDto signedInToApp(GithubUserDto githubUserDto) {
        var existingGithubUser = repository.getGithubUserByGlobalId(githubUserDto.globalId());

        var githubUser = githubUserMapper.togithubUser(githubUserDto);
        var newGithubUser = repository.save(githubUser);

        if (existingGithubUser == null) {
            var userFcm = GithubUserFcm.builder()
                    .fcmToken(githubUserDto.fcmToken())
                    .deviceId(githubUserDto.deviceId())
                    .enabled(false)
                    .githubUser(newGithubUser)
                    .signedOut(false)
                    .build();
            userFcm = githubUserFcmRepository.save(userFcm);
            return githubUserMapper.togithubUserResponseDto(newGithubUser, userFcm);
        } else  {
            var existingGithubUserFcm = githubUserFcmRepository.getGithubUserFcmByGithubUserAndDeviceId(
                    githubUser, githubUserDto.deviceId()
            );
            GithubUserFcm userFcm;
            if (existingGithubUserFcm == null) {
                userFcm = GithubUserFcm.builder()
                        .fcmToken(githubUserDto.fcmToken())
                        .deviceId(githubUserDto.deviceId())
                        .enabled(false)
                        .githubUser(newGithubUser)
                        .signedOut(false)
                        .build();
            } else {

                userFcm = GithubUserFcm.builder()
                        .id(existingGithubUserFcm.getId())
                        .fcmToken(githubUserDto.fcmToken())
                        .deviceId(githubUserDto.deviceId())
                        .enabled(existingGithubUserFcm.getEnabled())
                        .githubUser(newGithubUser)
                        .signedOut(false)
                        .build();
            }
            userFcm = githubUserFcmRepository.save(userFcm);
            return githubUserMapper.togithubUserResponseDto(newGithubUser, userFcm);

        }
    }

    @Transactional
    public void signOutFromApp(GithubUserDto githubUserDto) {
        var existingGithubUserFcm = githubUserFcmRepository.getGithubUserFcmByGithubUserAndDeviceId(
                githubUserMapper.togithubUser(githubUserDto), githubUserDto.deviceId()
        );
        if (existingGithubUserFcm != null) {
            existingGithubUserFcm.setSignedOut(true);
            existingGithubUserFcm.setSignedOutAt(Instant.now());
            githubUserFcmRepository.save(existingGithubUserFcm);
        }
    }

    public void updateFcmEnabled(FcmEnabledRequest fcmEnabledRequest) {
        var existingGithubUserFcm = githubUserFcmRepository.getGithubUserFcmByGithubUserAndDeviceId(
                GithubUser.builder().globalId(fcmEnabledRequest.globalId()).build(), fcmEnabledRequest.deviceId()
        );
        if (existingGithubUserFcm != null) {
            if (existingGithubUserFcm.getDeviceId().equals(fcmEnabledRequest.deviceId())) {
                existingGithubUserFcm.setEnabled(fcmEnabledRequest.fcmEnabled());
                existingGithubUserFcm.setEnabledUpdatedAt(Instant.now());
                githubUserFcmRepository.save(existingGithubUserFcm);
                return;
            }
        }

        throw new IllegalStateException("To update notification preference, server should have a record for user with " +
                "globald = " + fcmEnabledRequest.globalId() + " and deviceId = "+fcmEnabledRequest.deviceId()+", " +
                "but was not found");
    }

    public void updateFcmToken(UpdateFcmTokenRequest updateFcmTokenRequest) {
        var existingGithubUserFcm = githubUserFcmRepository.getGithubUserFcmByGithubUserAndDeviceId(
                GithubUser.builder().globalId(updateFcmTokenRequest.globalId()).build(), updateFcmTokenRequest.deviceId()
        );
        if (existingGithubUserFcm != null) {
            if (existingGithubUserFcm.getDeviceId().equals(updateFcmTokenRequest.deviceId())) {
                existingGithubUserFcm.setFcmToken(updateFcmTokenRequest.fcmToken());
                existingGithubUserFcm.setEnabledUpdatedAt(Instant.now());
                githubUserFcmRepository.save(existingGithubUserFcm);
                return;
            }
        }

        throw new IllegalStateException("To update fcm token, server should have a record for user with " +
                "globald = " + updateFcmTokenRequest.globalId() + " and deviceId = "+updateFcmTokenRequest.deviceId()+", " +
                "but was not found");
    }

    public void processWebhook(String sha, String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        GithubStarWebhookPayload payload;
        try {
            payload = objectMapper.readValue(body, GithubStarWebhookPayload.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var signature = HmacUtils.calculateHmac(githubServerProperties.getHmac_key(), body);
        System.out.println(githubServerProperties.getHmac_key());
        System.out.println("sha: "+sha);
        System.out.println("signature: " + signature);
        System.out.println("Github Webhook Delivery Header: " + signature.equals(sha));
        System.out.println("Github Webhook Delivery Message: " + payload.toString());

        var user = githubWebClient
                .get()
                .uri("/user/" + payload.repository.owner.id)
                .header("X-Github-Next-Global-ID", "1")
                .retrieve()
                .bodyToMono(GithubStarWebhookPayload.User.class)
                .block(REQUEST_TIMEOUT);
        System.out.println("Github Webhook Repository User" + user);
        if (user == null) { return; }

        var sender = githubWebClient
                .get()
                .uri("/user/" + payload.sender.id)
                .header("X-Github-Next-Global-ID", "1")
                .retrieve()
                .bodyToMono(GithubWebhookUserDetails.class)
                .block(REQUEST_TIMEOUT);

        // get fcm tokens for user
        var fcms = githubUserFcmRepository.getGithubUserFcmsByGithubUser_GlobalIdAndEnabledAndSignedOut(user.node_id, true, false);
        List<String> registrationTokens = fcms.stream().map(GithubUserFcm::getFcmToken).collect(Collectors.toList());
        if (sender != null && !registrationTokens.isEmpty()) {
        fcmService.sendFcmMessages(payload, sender, registrationTokens);
        }
    }
}
