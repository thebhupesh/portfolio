package com.bhupesh.portfolio.message;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Message {

    private String id;

    @NotBlank(message = "Sender name is required")
    @Size(max = 200, message = "Sender name must be at most 200 characters")
    private String senderName;

    @NotBlank(message = "Sender email is required")
    @Email(message = "Sender email must be a valid email address")
    @Size(max = 254, message = "Sender email must be at most 254 characters")
    private String senderEmail;

    @NotBlank(message = "Sender contact is required")
    @Size(max = 50, message = "Sender contact must be at most 50 characters")
    private String senderContact;

    @NotBlank(message = "Subject is required")
    @Size(max = 300, message = "Subject must be at most 300 characters")
    private String subject;

    @NotBlank(message = "Message body is required")
    @Size(max = 10000, message = "Message body must be at most 10000 characters")
    private String messageBody;

    @Size(max = 200, message = "Website field must be at most 200 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String website;

    @Size(max = 2048, message = "CAPTCHA token must be at most 2048 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String turnstileToken;

    private Instant timestamp;

    public void setTimestamp(Instant now) {
        this.timestamp = now;
    }
}
