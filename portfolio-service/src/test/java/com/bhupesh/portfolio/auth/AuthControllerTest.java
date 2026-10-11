package com.bhupesh.portfolio.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import com.bhupesh.portfolio.dto.Response;

class AuthControllerTest {

    private AuthService authenticationService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthService.class);
        controller = new AuthController(authenticationService);
    }

    @Test
    void createsTokenFromBasicAuthorizationHeader() {
        TokenResponse expected = new TokenResponse("signed-token", "Bearer", 900);
        when(authenticationService.authenticate("reader", "read-password")).thenReturn(expected);
        String encodedCredentials = Base64.getEncoder().encodeToString(
                "reader:read-password".getBytes(StandardCharsets.UTF_8));

        Response<TokenResponse> response = controller.createToken(
                        "Basic " + encodedCredentials)
                .getBody();

        assertEquals(expected, response.data());
        verify(authenticationService).authenticate("reader", "read-password");
    }

    @Test
    void rejectsMalformedBasicAuthorizationHeader() {
        assertThrows(BadCredentialsException.class,
                () -> controller.createToken("Basic invalid-base64"));
    }

    @Test
    void rejectsMissingAuthorizationHeader() {
        assertThrows(BadCredentialsException.class,
                () -> controller.createToken(null));
    }
}
