package com.edital.fullstack.auth;

import com.edital.fullstack.security.JwtService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final JwtService jwtService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final long refreshTtlDays;

  public AuthService(
      JwtService jwtService,
      RefreshTokenRepository refreshTokenRepository,
      @Value("${security.jwt.refresh-ttl-days}") long refreshTtlDays) {
    this.jwtService = jwtService;
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTtlDays = refreshTtlDays;
  }

  public AuthTokens login(String email, String password) {
    // Placeholder auth. Replace with real user validation.
    if (!"admin@local".equalsIgnoreCase(email) || !"admin".equals(password)) {
      throw new RuntimeException("Invalid credentials");
    }
    return issueTokens(UUID.fromString("00000000-0000-0000-0000-000000000001"));
  }

  public AuthTokens refresh(String refreshToken) {
    var token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
    if (token.isRevoked() || token.getExpiresAt().isBefore(Instant.now())) {
      throw new RuntimeException("Invalid refresh token");
    }
    token.setRevoked(true);
    token.setUpdatedAt(Instant.now());
    refreshTokenRepository.save(token);
    return issueTokens(token.getUserId());
  }

  public void logout(String refreshToken) {
    var token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();
    token.setRevoked(true);
    token.setUpdatedAt(Instant.now());
    refreshTokenRepository.save(token);
  }

  private AuthTokens issueTokens(UUID userId) {
    var accessToken = jwtService.generateAccessToken(userId.toString());
    var refresh = new RefreshToken(
        UUID.randomUUID(),
        userId,
        UUID.randomUUID().toString(),
        Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS),
        false,
        Instant.now(),
        Instant.now());
    refreshTokenRepository.save(refresh);
    return new AuthTokens(accessToken, refresh.getToken(), 300);
  }
}
