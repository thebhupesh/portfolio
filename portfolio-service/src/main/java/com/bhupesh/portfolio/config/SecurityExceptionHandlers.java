package com.bhupesh.portfolio.config;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.bhupesh.portfolio.dto.Response;

import tools.jackson.databind.ObjectMapper;

@Component
public class SecurityExceptionHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityExceptionHandlers.class);

    private final ObjectMapper objectMapper;

    public SecurityExceptionHandlers(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        LOGGER.warn("Unauthenticated request rejected: method={}", request.getMethod());
        response.setHeader("WWW-Authenticate", "Bearer");
        writeError(response, HttpStatus.UNAUTHORIZED, "Authentication is required or the token is invalid");
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {
        LOGGER.warn("Request denied by authorization rules: method={}", request.getMethod());
        writeError(response, HttpStatus.FORBIDDEN, "You do not have permission to perform this operation");
    }

    private void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), Response.<List<String>>builder()
                .success(false)
                .message(message)
                .build());
    }
}
