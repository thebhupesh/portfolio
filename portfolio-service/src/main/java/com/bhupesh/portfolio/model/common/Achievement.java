package com.bhupesh.portfolio.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Achievement {

    @NotBlank(message = "Achievement name is required")
    @Size(max = 500, message = "Achievement name must be at most 500 characters")
    private String name;

    @NotBlank(message = "Achievement date is required")
    @Size(max = 32, message = "Achievement date must be at most 32 characters")
    private String date;

    @Size(max = 2048, message = "Achievement description must be at most 2048 characters")
    private String description;

    @Size(max = 2048, message = "Achievement document key must be at most 2048 characters")
    private String document;
}
