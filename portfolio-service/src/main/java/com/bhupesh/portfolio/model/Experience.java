package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;
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
public class Experience extends BaseModel {

    private String company;
    private String location;
    private String startDate;
    private String endDate;
    private String role;
    private String description;
    private String techStack;
    private List<Achievement> achievements;
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
