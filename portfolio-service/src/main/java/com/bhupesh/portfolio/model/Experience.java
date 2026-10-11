package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;
import com.bhupesh.portfolio.model.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Experience extends BaseModel {

    @NotBlank(message = "Company is required")
    @Size(max = 200, message = "Company must be at most 200 characters")
    private String company;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;

    @NotBlank(message = "Start date is required")
    @Size(max = 32, message = "Start date must be at most 32 characters")
    private String startDate;

    @Size(max = 32, message = "End date must be at most 32 characters")
    private String endDate;

    @NotBlank(message = "Role is required")
    @Size(max = 200, message = "Role must be at most 200 characters")
    private String role;

    @Size(max = 5000, message = "Description must be at most 5000 characters")
    private String description;

    @Size(max = 2000, message = "Tech stack must be at most 2000 characters")
    private String techStack;

    @Valid
    @Size(max = 100, message = "At most 100 achievements are allowed")
    private List<Achievement> achievements;

    @Size(max = 2048, message = "Logo key must be at most 2048 characters")
    private String logo;

    @DynamoDbPartitionKey
    public String getType() {
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }

    @DynamoDbAttribute("start_date")
    public String getStartDate() {
        return startDate;
    }

    @DynamoDbAttribute("end_date")
    public String getEndDate() {
        return endDate;
    }

    @DynamoDbAttribute("tech_stack")
    public String getTechStack() {
        return techStack;
    }
}
