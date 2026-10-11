package com.bhupesh.portfolio.message;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhupesh.portfolio.dto.Response;

@RestController
@RequestMapping("/v1/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Response<List<String>>> sendMessage(@RequestBody Message message, HttpServletRequest request) {
        return messageService.validateSubmission(message, request)
                .map(error -> ResponseEntity.badRequest().body(error))
                .orElseGet(() -> {
                    messageService.send(message);
                    return ResponseEntity.ok(Response.<List<String>>builder()
                        .success(true)
                        .message("Message sent successfully")
                        .build());
                });
    }

    @PostMapping("/validate")
    public ResponseEntity<Response<List<String>>> validateMessage(@RequestBody Message message){
        return messageService.validateMessage(message)
                .map(error -> ResponseEntity.badRequest().body(error))
                .orElseGet(() -> ResponseEntity.ok(Response.<List<String>>builder()
                        .success(true)
                        .message("Message is valid")
                        .build()));
    }
}
