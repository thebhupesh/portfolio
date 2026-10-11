package com.bhupesh.portfolio.message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.validation.RequestValidator;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class MessageService {

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final RequestValidator requestValidator;

    public MessageService(
            SqsClient sqsClient,
            @Value("${aws.sqs.message-queue-url}") String queueUrl,
            RequestValidator requestValidator) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.requestValidator = requestValidator;
    }

    public void send(Message message) {
        Instant now = Instant.now();
        message.setId(UUID.randomUUID().toString());
        message.setTimestamp(now);

        String body = new ObjectMapper()
                .writeValueAsString(message);

        SendMessageRequest request =
                SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(body)
                        .build();

        sqsClient.sendMessage(request);
    }

    public Optional<Response<List<String>>> validateMessage(Message message) {
        List<String> validationErrors = requestValidator.validate(message);
        if (!validationErrors.isEmpty()) {
            return Optional.of(requestValidator.errorResponse("Validation failed", validationErrors));
        }

        return Optional.empty();
    }

    public Optional<Response<List<String>>> validateSubmission(Message message, HttpServletRequest request) {
        Optional<Response<List<String>>> validationError = validateMessage(message);
        if (validationError.isPresent()) {
            return validationError;
        }

        // TODO: Uncomment the following code when spam protection and CAPTCHA verification are implemented

        // String clientIp = Optional.ofNullable(request)
        //         .map(HttpServletRequest::getRemoteAddr)
        //         .orElse("");
        // List<String> captchaErrors = turnstileVerificationService.validate(message, clientIp);
        // if (!captchaErrors.isEmpty()) {
        //     return Optional.of(requestValidator.errorResponse("CAPTCHA verification failed", captchaErrors));
        // }

        // List<String> spamErrors = spamProtectionService.validateSubmission(message, clientIp);
        // if (!spamErrors.isEmpty()) {
        //     return Optional.of(requestValidator.errorResponse("Message rejected", spamErrors));
        // }

        return Optional.empty();
    }
}