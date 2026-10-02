package com.bhupesh.portfolio.controller;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.model.MessageModel;
import com.bhupesh.portfolio.model.ResponseModel;
import com.bhupesh.portfolio.service.MessageService;

@RestController
@RequestMapping("/v1/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> send(@RequestBody MessageModel message){

        message.setType("message");
        message.setId(UUID.randomUUID().toString());
        message.setCreatedAt(Instant.now());

        ResponseModel response = messageService.isValid(message);

        if(response.getStatus().equals("success")) {
            messageService.send(message);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
