package com.shyampatel.webapp.githubplayroom;

import com.google.auth.oauth2.ClientId;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.shyampatel.webapp.githubplayroom.auth.AuthenticationService;
import com.shyampatel.webapp.githubplayroom.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.FileInputStream;
import java.io.IOException;

import static com.shyampatel.webapp.githubplayroom.user.Role.ADMIN;
import static com.shyampatel.webapp.githubplayroom.user.Role.MANAGER;

@SpringBootApplication
public class GithubPlayroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubPlayroomApplication.class, args);

		try {
		FileInputStream serviceAccount =
				new FileInputStream("src/main/resources/github-playroom-firebase-adminsdk.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
			FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			@Value("${application.startup.credentials.admin_username}") String adminUsername,
			@Value("${application.startup.credentials.admin_password}") String adminPassword
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email(adminUsername)
					.password(adminPassword)
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());
		};
	}
}
