package com.bhupesh.portfolio.exception;

import java.io.IOException;
import java.util.List;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.bhupesh.portfolio.dto.Response;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<List<String>>> handleBadCredentials() {
        LOGGER.warn("Authentication rejected because credentials were invalid");
        return error(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<List<String>>> handleInvalidArguments(
            MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .toList();
        return validationError(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<List<String>>> handleConstraintViolations(
            ConstraintViolationException exception) {
        List<String> errors = exception.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .sorted()
                .toList();
        return validationError(errors);
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Response<List<String>>> handleMalformedRequest(Exception exception) {
        return error(HttpStatus.BAD_REQUEST, "Request is malformed or missing required values");
    }

    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<Response<List<String>>> handleAwsServiceFailure(AwsServiceException exception) {
        if (exception.statusCode() == HttpStatus.NOT_FOUND.value()) {
            return error(HttpStatus.NOT_FOUND, "Requested resource was not found");
        }
        LOGGER.error("AWS service request failed with status {}", exception.statusCode(), exception);
        return error(HttpStatus.SERVICE_UNAVAILABLE, "A required service is temporarily unavailable");
    }

    @ExceptionHandler(SdkException.class)
    public ResponseEntity<Response<List<String>>> handleAwsFailure(SdkException exception) {
        LOGGER.error("AWS service request failed", exception);
        return error(HttpStatus.SERVICE_UNAVAILABLE, "A required service is temporarily unavailable");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Response<List<String>>> handleIoFailure(IOException exception) {
        LOGGER.error("I/O operation failed while handling an API request", exception);
        return error(HttpStatus.BAD_GATEWAY, "File operation could not be completed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<List<String>>> handleUnexpectedFailure(Exception exception) {
        if (exception instanceof ErrorResponse errorResponse) {
            HttpStatusCode status = errorResponse.getStatusCode();
            if (status.is5xxServerError()) {
                LOGGER.error("Framework request processing failed with status {}", status.value(), exception);
            }
            String message = status.value() == HttpStatus.NOT_FOUND.value()
                    ? "Requested resource was not found"
                    : "Request could not be completed";
            return error(status, message);
        }
        LOGGER.error("Unhandled API request failure", exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred");
    }

    private ResponseEntity<Response<List<String>>> validationError(List<String> errors) {
        return ResponseEntity.badRequest().body(Response.<List<String>>builder()
                .success(false)
                .message("Validation failed")
                .data(errors)
                .build());
    }

    private ResponseEntity<Response<List<String>>> error(HttpStatusCode status, String message) {
        return ResponseEntity.status(status).body(Response.<List<String>>builder()
                .success(false)
                .message(message)
                .build());
    }
}
