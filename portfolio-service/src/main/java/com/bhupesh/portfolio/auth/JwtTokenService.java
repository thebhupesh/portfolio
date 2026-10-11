package com.bhupesh.portfolio.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration tokenTtl;

    public JwtTokenService(JwtEncoder jwtEncoder, @Value("${security.jwt.ttl}") Duration tokenTtl) {
        if (tokenTtl.toSeconds() < 1) {
            throw new IllegalArgumentException("JWT token lifetime must be at least one second");
        }
        this.jwtEncoder = jwtEncoder;
        this.tokenTtl = tokenTtl;
    }

    public TokenResponse createToken(String username, String role) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(tokenTtl);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("roles", List.of(role))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims)).getTokenValue();

        return new TokenResponse(token, "Bearer", tokenTtl.toSeconds());
    }
}
