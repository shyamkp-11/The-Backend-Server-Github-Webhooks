package com.shyampatel.webapp.githubplayroom.githubuser;

import com.google.firebase.messaging.*;
import com.shyampatel.webapp.githubplayroom.githubuserfcm.GithubUserFcmRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FcmService {

    public static final int MAX_FAILURE_COUNT = 5;
    private final GithubUserFcmRepository githubUserFcmRepository;

    public FcmService(GithubUserFcmRepository githubUserFcmRepository) {
        this.githubUserFcmRepository = githubUserFcmRepository;
    }

    public void sendFcmMessages(GithubStarWebhookPayload payload, GithubWebhookUserDetails sender, List<String> registrationTokens) {
        if (registrationTokens.isEmpty() || payload == null || sender == null) throw new IllegalArgumentException();
        // not sending notification for unstars.
        if (payload.action.equals("deleted")) return;
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification
                        .builder()
                        .setTitle("⭐+ "+payload.repository.name)
                        .setBody(
                                "by "+ sender.login()
                        )
                        .setImage(payload.sender.avatar_url)
                        .build())
                .addAllTokens(registrationTokens)
                .build();
        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(registrationTokens.get(i));
                    var githubUserFcms = githubUserFcmRepository.getGithubUserFcmsByFcmToken(registrationTokens.get(i));
                    githubUserFcms.forEach(it ->
                            {
                                it.setMessageDeliverFailureCount(it.getMessageDeliverFailureCount()+1);
                                if (it.getMessageDeliverFailureCount() >= MAX_FAILURE_COUNT) {
                                    it.setEnabled(false);
                                }
                            }
                    );
                    githubUserFcmRepository.saveAll(githubUserFcms);

                } else  {
                    var githubUserFcms = githubUserFcmRepository.getGithubUserFcmsByFcmToken(registrationTokens.get(i));
                    githubUserFcms.forEach(it ->
                            it.setMessageDeliverFailureCount(0)
                    );
                    githubUserFcmRepository.saveAll(githubUserFcms);
                }
            }
        }
    }
}
