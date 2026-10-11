package com.bhupesh.portfolio.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.server.ResponseStatusException;

import com.bhupesh.portfolio.dto.Response;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void mapsBadCredentialsToUnauthorizedWithoutCredentialDetails() {
        var response = handler.handleBadCredentials();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody().message());
    }

    @Test
    void returnsSortedFieldErrorsForInvalidBody() throws NoSuchMethodException {
        InvalidRequest request = new InvalidRequest("", "");
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(request, "loginRequest");
        bindingResult.rejectValue("username", "NotBlank", "username is required");
        bindingResult.rejectValue("password", "NotBlank", "password is required");
        Method method = ApiExceptionHandlerTest.class.getDeclaredMethod("invalidRequest", InvalidRequest.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(method, 0),
                bindingResult);

        var response = handler.handleInvalidArguments(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(List.of(
                "password: password is required",
                "username: username is required"), response.getBody().data());
        assertFalse(response.getBody().success());
    }

    @Test
    void returnsClientErrorForMalformedRequestWithoutLeakingParserDetails() {
        var response = handler.handleMalformedRequest(
                new HttpMessageNotReadableException(
                        "private parser detail",
                        org.mockito.Mockito.mock(HttpInputMessage.class)));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Request is malformed or missing required values", response.getBody().message());
        assertFalse(response.getBody().message().contains("private parser detail"));
    }

    @Test
    void mapsMissingQueryParameterToBadRequest() {
        var response = handler.handleMalformedRequest(
                new MissingServletRequestParameterException("file", "MultipartFile"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void mapsAwsDependencyFailureToServiceUnavailableWithoutExposingDetails() {
        var response = handler.handleAwsFailure(SdkClientException.create("private AWS detail"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("A required service is temporarily unavailable", response.getBody().message());
        assertFalse(response.getBody().message().contains("private AWS detail"));
    }

    @Test
    void mapsAwsResourceNotFoundToNotFoundAndOtherAwsErrorsToServiceUnavailable() {
        ResponseEntity<Response<List<String>>> missing = handler.handleAwsServiceFailure(AwsServiceException.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message("private AWS detail")
                .build());
        ResponseEntity<Response<List<String>>> unavailable = handler.handleAwsServiceFailure(
                AwsServiceException.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("private AWS detail")
                .build());

        assertEquals(HttpStatus.NOT_FOUND, missing.getStatusCode());
        assertEquals("Requested resource was not found", missing.getBody().message());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, unavailable.getStatusCode());
    }

    @Test
    void mapsIoFailureAndUnexpectedFailureToSafeResponses() {
        var ioResponse = handler.handleIoFailure(new IOException("private file detail"));
        var unexpectedResponse = handler.handleUnexpectedFailure(
                new IllegalStateException("private internal detail"));

        assertEquals(HttpStatus.BAD_GATEWAY, ioResponse.getStatusCode());
        assertEquals("File operation could not be completed", ioResponse.getBody().message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, unexpectedResponse.getStatusCode());
        assertEquals("An unexpected server error occurred", unexpectedResponse.getBody().message());
        assertNull(unexpectedResponse.getBody().data());
    }

    @Test
    void preservesFrameworkHttpStatusWithoutReturningInternalReason() {
        ResponseStatusException exception =
                new ResponseStatusException(HttpStatus.NOT_FOUND, "private internal detail");

        var response = handler.handleUnexpectedFailure(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Requested resource was not found", response.getBody().message());
        assertFalse(response.getBody().message().contains("private internal detail"));
    }

    @SuppressWarnings("unused")
    private void invalidRequest(InvalidRequest request) {
    }

    private record InvalidRequest(String username, String password) {
    }
}
