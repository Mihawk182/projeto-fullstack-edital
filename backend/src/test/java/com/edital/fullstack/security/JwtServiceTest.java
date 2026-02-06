package com.edital.fullstack.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JwtServiceTest {
  @Test
  void generateAndParseToken() {
    var secret = "change-me-please-32-chars-minimum";
    var service = new JwtService(secret, 5);

    var token = service.generateAccessToken("user-123");
    var claims = service.parse(token);

    assertEquals("user-123", claims.getSubject());
    assertTrue(claims.getExpiration().getTime() > claims.getIssuedAt().getTime());
  }
}
