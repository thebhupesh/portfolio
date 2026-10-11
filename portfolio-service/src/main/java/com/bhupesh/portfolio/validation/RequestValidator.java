package com.bhupesh.portfolio.validation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bhupesh.portfolio.dto.Response;

import jakarta.validation.Validator;

@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public List<String> validate(Object request) {
        if (request == null) {
            return List.of("Request body is required");
        }

        return validator.validate(request).stream()
                .map(error -> error.getPropertyPath() + ": " + error.getMessage())
                .sorted()
                .toList();
    }

    public Response<List<String>> errorResponse(String message, List<String> errors) {
        return Response.<List<String>>builder()
                .success(false)
                .message(message)
                .data(errors)
                .build();
    }
}
