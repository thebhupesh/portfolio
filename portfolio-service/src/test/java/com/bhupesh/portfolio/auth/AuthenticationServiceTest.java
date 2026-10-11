package com.bhupesh.portfolio.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {

    private DynamoDbCredentialStore credentialStore;
    private PasswordEncoder passwordEncoder;
    private JwtTokenService jwtTokenService;
    private AuthService authenticationService;

    @BeforeEach
    void setUp() {
        credentialStore = mock(DynamoDbCredentialStore.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenService = mock(JwtTokenService.class);
        when(passwordEncoder.encode(anyString())).thenReturn("dummy-hash");
        authenticationService = new AuthService(
                credentialStore,
                passwordEncoder,
                jwtTokenService);
    }

    @Test
    void issuesTokenForNormalizedReadCredential() {
        when(credentialStore.findByUsername("reader"))
                .thenReturn(Optional.of(new DynamoDbCredentialStore.StoredCredential("stored-hash", "READ")));
        when(passwordEncoder.matches("read-password", "stored-hash")).thenReturn(true);
        TokenResponse expected = new TokenResponse("signed-token", "Bearer", 900);
        when(jwtTokenService.createToken("reader", "READ")).thenReturn(expected);

        TokenResponse actual = authenticationService.authenticate(" Reader ", "read-password");

        assertEquals(expected, actual);
        verify(jwtTokenService).createToken("reader", "READ");
    }

    @Test
    void issuesWriteRoleTokenForWriteCredential() {
        when(credentialStore.findByUsername("writer"))
                .thenReturn(Optional.of(new DynamoDbCredentialStore.StoredCredential("stored-hash", "WRITE")));
        when(passwordEncoder.matches("write-password", "stored-hash")).thenReturn(true);
        TokenResponse expected = new TokenResponse("signed-token", "Bearer", 900);
        when(jwtTokenService.createToken("writer", "WRITE")).thenReturn(expected);

        TokenResponse actual = authenticationService.authenticate("writer", "write-password");

        assertEquals(expected, actual);
        verify(jwtTokenService).createToken("writer", "WRITE");
    }

    @Test
    void rejectsUnknownUsernameAfterPasswordHashComparison() {
        when(credentialStore.findByUsername("missing")).thenReturn(Optional.empty());
        when(passwordEncoder.matches("password", "dummy-hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate("missing", "password"));
    }

    @Test
    void rejectsUnsupportedStoredRole() {
        when(credentialStore.findByUsername("user"))
                .thenReturn(Optional.of(new DynamoDbCredentialStore.StoredCredential("stored-hash", "ADMIN")));
        when(passwordEncoder.matches("password", "stored-hash")).thenReturn(true);

        assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate("user", "password"));
    }

    @Test
    void rejectsPasswordsThatExceedBcryptInputLimit() {
        assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate("user", "p".repeat(73)));
    }
}
