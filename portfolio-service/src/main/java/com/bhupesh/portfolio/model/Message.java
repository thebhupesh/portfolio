package com.bhupesh.portfolio.model;

import java.time.Instant;

import com.bhupesh.portfolio.model.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean 
@Data 
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
public class Message extends BaseModel {

    private String senderName;
    private String senderEmail;
    private String senderContact;
    private String subject;
    private String messageBody;
    private Instant timestamp;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getType() {
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }
    
    @DynamoDbAttribute("sender_name")
    public String getSenderName() {
        return senderName;
    }

    @DynamoDbAttribute("sender_email")
    public String getSenderEmail() {
        return senderEmail;
    }

    @DynamoDbAttribute("sender_contact")
    public String getSenderContact() {
        return senderContact;
    }

    @DynamoDbAttribute("message_body")
    public String getMessageBody() {
        return messageBody;
    }

    @DynamoDbAttribute("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setTimestamp(Instant now) {
        this.timestamp = now;
    }
}
