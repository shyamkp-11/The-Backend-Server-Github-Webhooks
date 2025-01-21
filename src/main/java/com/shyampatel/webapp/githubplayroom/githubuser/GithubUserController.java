package com.shyampatel.webapp.githubplayroom.githubuser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyampatel.webapp.githubplayroom.HmacUtils;
import com.shyampatel.webapp.githubplayroom.config.GithubServerProperties;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequestMapping("/api/v1/githubUsers")
@RestController
public class GithubUserController {

    private final GithubUserService githubUserService;

    @Autowired
    public GithubUserController(GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }

    @PostMapping("/signedInToApp")
    public GithubUserResponseDto signedInToApp(@RequestBody @Valid GithubUserDto githubUser) {
        return githubUserService.signedInToApp(githubUser);
    }

    @PostMapping("signout")
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
