package com.bhupesh.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bhupesh.portfolio.model.MessageModel;
import com.bhupesh.portfolio.model.ResponseModel;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class MessageService {

    private final SqsClient sqsClient;
    private final String queueUrl;

    public MessageService(
            SqsClient sqsClient,
            @Value("${aws.sqs.message-queue-url}") String queueUrl
    ) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    public void send(MessageModel message) {

        String body = new ObjectMapper()
                .writeValueAsString(message);

        SendMessageRequest request =
                SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(body)
                        .build();

        sqsClient.sendMessage(request);
    }

    public ResponseModel isValid(MessageModel message) {
        return new ResponseModel("success", "Message is valid");
    }
}