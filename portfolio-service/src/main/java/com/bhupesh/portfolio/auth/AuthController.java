package com.bhupesh.portfolio.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")
    public ResponseEntity<Response<TokenResponse>> createToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        BasicCredentials credentials = parseBasicCredentials(authorizationHeader);
        TokenResponse token = authenticationService.authenticate(credentials.username(), credentials.password());
        return ResponseEntity.ok(Response.<TokenResponse>builder()
                .success(true)
                .data(token)
                .build());
    }

    private BasicCredentials parseBasicCredentials(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        String[] parts = authorizationHeader.trim().split("\\s+", 2);
        if (parts.length != 2 || !"Basic".equalsIgnoreCase(parts[0])) {
            throw new BadCredentialsException("Invalid username or password");
        }

        final String decoded;
        try {
            decoded = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException exception) {
            throw new BadCredentialsException("Invalid username or password", exception);
        }

        int separator = decoded.indexOf(':');
        if (separator <= 0) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String username = decoded.substring(0, separator);
        String password = decoded.substring(separator + 1);
        if (username.isBlank() || username.length() > 128
                || password.isBlank() || password.length() > 1024) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new BasicCredentials(username, password);
    }

    private record BasicCredentials(String username, String password) {
    }
}
