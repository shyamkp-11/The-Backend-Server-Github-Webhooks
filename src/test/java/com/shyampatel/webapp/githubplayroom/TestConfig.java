package com.shyampatel.webapp.githubplayroom;

import com.shyampatel.webapp.githubplayroom.config.JwtAuthenticationFilter;
import com.shyampatel.webapp.githubplayroom.config.JwtService;
import com.shyampatel.webapp.githubplayroom.token.TokenRepository;
import com.shyampatel.webapp.githubplayroom.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.sql.DataSource;
import java.io.IOException;

@TestConfiguration
    public class TestConfig {

        @MockitoBean
        public JwtService jwtService;
        @MockitoBean
        public UserDetailsService userDetailsService;
        @MockitoBean
        public TokenRepository tokenRepository;
        @Bean
        @Primary
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository) {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                    filterChain.doFilter(request, response);
                }
            };
        }

        @Bean
        public JwtService getJwtService() {
            return jwtService;
        }

        @Bean
        public TokenRepository getTokenRepository() {
            return tokenRepository;
        }

        @Bean
        public LogoutHandler getLogoutHandler() {
            return Mockito.mock(LogoutHandler.class);
        }

        @Bean
        public AuthenticationProvider getAuthenticationProvider() {
            return Mockito.mock(AuthenticationProvider.class);
        }

        @Bean
        public UserDetailsService getUserDetailsService() {
            return Mockito.mock(UserDetailsService.class);
        }

        @Bean
        public UserRepository getUserRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public DataSource getDataSource() {
            return Mockito.mock(DataSource.class);
        }

        @PostConstruct
        public void postConstruct() {

        }
    }