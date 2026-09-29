package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class ExperienceModel {
    
    private String type;
    private String id;
    private String company;
    private String location;
    private String startDate;
    private String endDate;
    private String role;
    private String description;
    private String techStack;
    private List<Achievement> achievements;

    @DynamoDbPartitionKey
    public String getType() {
        return type;
    }

    @DynamoDbSortKey
    public String getId() {
        return id;
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
