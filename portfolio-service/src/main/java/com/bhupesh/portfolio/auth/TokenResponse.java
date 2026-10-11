package com.bhupesh.portfolio.auth;

public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
