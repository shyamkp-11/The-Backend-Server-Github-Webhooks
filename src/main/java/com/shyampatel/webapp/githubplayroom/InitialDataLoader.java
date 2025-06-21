package com.shyampatel.webapp.githubplayroom;

import com.shyampatel.webapp.githubplayroom.auth.AuthenticationService;
import com.shyampatel.webapp.githubplayroom.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.shyampatel.webapp.githubplayroom.user.Role.ADMIN;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final AuthenticationService authenticationService;
    @Value("${application.startup.credentials.admin_username}") String adminUsername;
    @Value("${application.startup.credentials.admin_password}") String adminPassword;
    @Autowired
    public InitialDataLoader(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            var admin = RegisterRequest.builder()
                    .setFirstname("Admin")
                    .setLastname("Admin")
                    .setEmail(adminUsername)
                    .setPassword(adminPassword)
                    .setRole(ADMIN)
                    .build();
            System.out.println("Admin token: " + authenticationService.register(admin).getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
