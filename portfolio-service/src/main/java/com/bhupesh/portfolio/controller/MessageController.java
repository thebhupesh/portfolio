package com.bhupesh.portfolio.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;
import com.bhupesh.portfolio.model.Message;
import com.bhupesh.portfolio.service.MessageService;

@RestController
@RequestMapping("/v1/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Response<List<String>>> sendMessage(@RequestBody Message message){
        List<String> response = messageService.isValidMessage(message);

        if(response.size() == 0) {
            message.setType("message");
            message.setId(UUID.randomUUID().toString());
            message.setTimestamp(Instant.now());
            
            messageService.send(message);
            
            return ResponseEntity.ok(Response.<List<String>>builder()
                    .success(true)
                    .message("Message sent successfully")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(Response.<List<String>>builder()
                    .success(false)
                    .message("Invalid message")
                    .data(response)
                    .build());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Response<List<String>>> validateMessage(@RequestBody Message message){
        List<String> response = messageService.isValidMessage(message);

        if(response.size() == 0) {
            return ResponseEntity.ok(Response.<List<String>>builder()
                    .success(true)
                    .message("Message is valid")
                    .build());
        } else {
            return ResponseEntity.badRequest().body(Response.<List<String>>builder()
                    .success(false)
                    .message("Invalid message")
                    .data(response)
                    .build());
        }
    }
}
