package com.bhupesh.portfolio.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import tools.jackson.databind.json.JsonMapper;

class SecurityExceptionHandlersTest {

    private final SecurityExceptionHandlers handlers =
            new SecurityExceptionHandlers(JsonMapper.builder().build());
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    void writesSafeJson401ResponseAndBearerChallenge() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        handlers.commence(request, response, new BadCredentialsException("private token detail"));

        assertEquals(401, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("Bearer", response.getHeader("WWW-Authenticate"));
        assertTrue(response.getContentAsString().contains("Authentication is required or the token is invalid"));
        assertFalse(response.getContentAsString().contains("private token detail"));
    }

    @Test
    void writesSafeJson403ResponseForInsufficientRole() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        handlers.handle(request, response, new AccessDeniedException("private authorization detail"));

        assertEquals(403, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertTrue(response.getContentAsString()
                .contains("You do not have permission to perform this operation"));
        assertFalse(response.getContentAsString().contains("private authorization detail"));
    }
}
