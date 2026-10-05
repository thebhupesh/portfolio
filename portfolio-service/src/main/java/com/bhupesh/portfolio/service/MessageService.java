package com.bhupesh.portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.Message;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class MessageService {

    private final SqsClient sqsClient;
    private final String queueUrl;

    public MessageService(SqsClient sqsClient, @Value("${aws.sqs.message-queue-url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    public void send(Message message) {
        String body = new ObjectMapper()
                .writeValueAsString(message);

        SendMessageRequest request =
                SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(body)
                        .build();

        sqsClient.sendMessage(request);
    }

    public List<String> isValidMessage(Message message) {
        return new ArrayList<>();
    }
}