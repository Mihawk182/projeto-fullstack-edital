package com.edital.fullstack.auth;

public record AuthTokens(String accessToken, String refreshToken, long expiresIn) {}
