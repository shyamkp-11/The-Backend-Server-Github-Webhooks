package com.shyampatel.webapp.githubplayroom.githubuser;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/githubUsers")
@RestController
public class GithubUserController {

    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @PostMapping("/signedInToApp")
    public GithubUserResponseDto signedInToApp(@Valid @RequestBody GithubUserDto githubUser) {
        var response = githubUserService.signedInToApp(githubUser);
        return response;
    }

    @PostMapping("/signout")
    public void signedOutFromApp(@RequestBody GithubUserDto githubUser) {
        githubUserService.signOutFromApp(githubUser);
    }

    @PutMapping("/fcmEnabled")
    public void fcmEnabled(@RequestBody FcmEnabledRequest fcmEnabledRequest) {
        githubUserService.updateFcmEnabled(fcmEnabledRequest);
    }

    @PutMapping("fcmToken")
    public void updateFcmToken(@RequestBody UpdateFcmTokenRequest updateFcmTokenRequest) {
        githubUserService.updateFcmToken(updateFcmTokenRequest);
    }

    @PostMapping("/githubWebhookDelivery")
    public void githubWebhookDelivery(@RequestHeader("X-Hub-Signature-256") @Nullable String sha, @RequestBody String body) throws JsonProcessingException {
        githubUserService.processWebhook(sha, body);
    }
}
