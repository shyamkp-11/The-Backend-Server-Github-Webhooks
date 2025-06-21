package com.shyampatel.webapp.githubplayroom;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class GithubPlayroomApplication {

	public static void main(String[] args) {
		System.out.println("Starting Github Playroom Application");
		SpringApplication.run(GithubPlayroomApplication.class, args);

		try {
		InputStream serviceAccount =
				new ClassPathResource(
						"github-playroom-firebase-adminsdk.json").getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
			FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}
