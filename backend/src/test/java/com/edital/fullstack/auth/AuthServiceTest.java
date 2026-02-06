package com.edital.fullstack.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edital.fullstack.security.JwtService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock
  JwtService jwtService;

  @Mock
  RefreshTokenRepository refreshTokenRepository;

  @Test
  void loginReturnsTokensForValidCredentials() {
    when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
    when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var service = new AuthService(jwtService, refreshTokenRepository, 7);
    var tokens = service.login("admin@local", "admin");

    assertEquals("access-token", tokens.accessToken());
    verify(refreshTokenRepository).save(any(RefreshToken.class));
  }

  @Test
  void loginRejectsInvalidCredentials() {
    var service = new AuthService(jwtService, refreshTokenRepository, 7);
    assertThrows(RuntimeException.class, () -> service.login("admin@local", "wrong"));
  }

  @Test
  void refreshRevokesOldTokenAndIssuesNewOne() {
    when(jwtService.generateAccessToken(anyString())).thenReturn("access-token");
    when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var userId = UUID.randomUUID();
    var existing = new RefreshToken(
        UUID.randomUUID(),
        userId,
        "refresh-token",
        Instant.now().plusSeconds(3600),
        false,
        Instant.now(),
        Instant.now());
    when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(Optional.of(existing));

    var service = new AuthService(jwtService, refreshTokenRepository, 7);
    var tokens = service.refresh("refresh-token");

    assertEquals("access-token", tokens.accessToken());
    verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
  }

  @Test
  void logoutRevokesToken() {
    var existing = new RefreshToken(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "refresh-token",
        Instant.now().plusSeconds(3600),
        false,
        Instant.now(),
        Instant.now());
    when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(Optional.of(existing));
    when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    var service = new AuthService(jwtService, refreshTokenRepository, 7);
    service.logout("refresh-token");

    var captor = ArgumentCaptor.forClass(RefreshToken.class);
    verify(refreshTokenRepository).save(captor.capture());
    assertEquals(true, captor.getValue().isRevoked());
  }
}
