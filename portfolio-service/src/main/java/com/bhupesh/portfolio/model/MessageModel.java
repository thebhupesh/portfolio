package com.bhupesh.portfolio.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class MessageModel {
    
    private String type;
    private String id;
    private String senderName;
    private String senderEmail;
    private String senderContact;
    private String subject;
    private String messageBody;
    private Instant timestamp;
    private String createdAt;

    public void setCreatedAt(Instant now) {
        this.timestamp = now;
    }
}
