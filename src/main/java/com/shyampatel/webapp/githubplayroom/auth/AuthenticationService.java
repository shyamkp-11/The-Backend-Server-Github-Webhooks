package com.shyampatel.webapp.githubplayroom.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyampatel.webapp.githubplayroom.config.JwtService;
import com.shyampatel.webapp.githubplayroom.token.Token;
import com.shyampatel.webapp.githubplayroom.token.TokenRepository;
import com.shyampatel.webapp.githubplayroom.token.TokenType;
import com.shyampatel.webapp.githubplayroom.user.Role;
import com.shyampatel.webapp.githubplayroom.user.User;
import com.shyampatel.webapp.githubplayroom.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(UserRepository repository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.repository = repository;
    this.tokenRepository = tokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
            .setFirstname(request.getFirstname())
            .setLastname(request.getLastname())
            .setEmail(request.getEmail())
            .setPassword(passwordEncoder.encode(request.getPassword()))
            .setRole(request.getRole())
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return new AuthenticationResponse(jwtToken, refreshToken);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return new AuthenticationResponse(jwtToken, refreshToken);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .setUser(user)
        .setToken(jwtToken)
        .setTokenType(TokenType.BEARER)
        .setExpired(false)
        .setRevoked(false)
        .build();
    tokenRepository.saveAndFlush(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = new AuthenticationResponse(accessToken, refreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public AuthenticationResponse createClientApiUser(RegisterRequest request) {
    var user = User.builder()
            .setFirstname(request.getFirstname())
            .setLastname(request.getLastname())
            .setEmail(request.getEmail())
            .setPassword(passwordEncoder.encode(request.getPassword()))
            .setRole(Role.USER)
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateTokenMaxLifetime(user);
    var refreshToken = jwtService.generateTokenMaxLifetime(user);
    saveUserToken(savedUser, jwtToken);
    return new AuthenticationResponse(jwtToken, refreshToken);
  }
}