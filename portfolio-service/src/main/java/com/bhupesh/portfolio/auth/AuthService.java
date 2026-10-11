package com.bhupesh.portfolio.auth;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String READ_ROLE = "READ";
    private static final String WRITE_ROLE = "WRITE";
    private static final int MAX_BCRYPT_PASSWORD_BYTES = 72;

    private final DynamoDbCredentialStore credentialStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final String dummyPasswordHash;

    public AuthService(
            DynamoDbCredentialStore credentialStore,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.credentialStore = credentialStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.dummyPasswordHash = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public TokenResponse authenticate(String username, String password) {
        if (password != null
                && password.getBytes(StandardCharsets.UTF_8).length > MAX_BCRYPT_PASSWORD_BYTES) {
            throw new BadCredentialsException("Invalid username or password");
        }
        Optional<String> normalizedUsername = Optional.ofNullable(username)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(value -> value.toLowerCase(Locale.ROOT));
        String candidatePassword = Optional.ofNullable(password).orElse("");
        Optional<DynamoDbCredentialStore.StoredCredential> credential =
                normalizedUsername.flatMap(credentialStore::findByUsername);
        String storedHash = credential.map(DynamoDbCredentialStore.StoredCredential::passwordHash)
                .filter(value -> !value.isBlank())
                .orElse(dummyPasswordHash);
        boolean passwordMatches = passwordEncoder.matches(candidatePassword, storedHash);

        if (credential.isEmpty() || !passwordMatches
                || !credential.map(DynamoDbCredentialStore.StoredCredential::role)
                        .filter(AuthService::isSupportedRole)
                        .isPresent()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return jwtTokenService.createToken(normalizedUsername.orElseThrow(),
                credential.orElseThrow().role());
    }

    private static boolean isSupportedRole(String role) {
        return READ_ROLE.equals(role) || WRITE_ROLE.equals(role);
    }
}
