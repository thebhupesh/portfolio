package com.bhupesh.portfolio.model.common;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class BaseModel {

    private String type;
    private String id;
    private Instant updatedAt;

    @DynamoDbAttribute("updated_at")
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
