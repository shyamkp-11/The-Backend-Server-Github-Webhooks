package com.shyampatel.webapp.githubplayroom;

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
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static com.shyampatel.webapp.githubplayroom.user.Role.ADMIN;

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

//	@Profile("dev")
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			@Value("${application.startup.credentials.admin_username}") String adminUsername,
			@Value("${application.startup.credentials.admin_password}") String adminPassword
	) {
		return args -> {
			try {
				var admin = RegisterRequest.builder()
						.setFirstname("Admin")
						.setLastname("Admin")
						.setEmail(adminUsername)
						.setPassword(adminPassword)
						.setRole(ADMIN)
						.build();
				System.out.println("Admin token: " + service.register(admin).getAccessToken());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}
