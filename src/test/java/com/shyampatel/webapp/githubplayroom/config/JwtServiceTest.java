package com.shyampatel.webapp.githubplayroom.config;

import com.shyampatel.webapp.githubplayroom.user.Role;
import com.shyampatel.webapp.githubplayroom.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void generateTokenMaxLifetime() {
        var token = jwtService.generateTokenMaxLifetime(User.builder().email("email").password("password").role(Role.USER).build());
        System.out.println(token);
        System.out.println(new Date(Long.MAX_VALUE).toInstant());
        assertNotNull(token);
    }
}