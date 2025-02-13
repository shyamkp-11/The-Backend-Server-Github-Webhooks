package com.shyampatel.webapp.githubplayroom.config;

import com.shyampatel.webapp.githubplayroom.user.Role;
import com.shyampatel.webapp.githubplayroom.user.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {


    private final JwtService jwtService = new JwtService();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "2b56a93b196bfd12d5740d42b5eb75033df51d02f46a665fe6c6245127f68a798ff45a1e0bc5dde98e8fb5b354813cfad6f8f1d7981b882f9b66ca2ae0af80d9");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000);
    }

    @Test
    void generateTokenMaxLifetime() {
        var user = User.builder()
                .setEmail("email")
                .setPassword("password")
                .setRole(Role.ADMIN)
                .build();
        var token = jwtService.generateTokenMaxLifetime(user);
        assertEquals((new Date(Long.MAX_VALUE)).toString(), jwtService.extractClaim(token, Claims::getExpiration).toString());
        assertNotNull(token);
    }
}