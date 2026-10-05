package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.BaseModel;
import com.bhupesh.portfolio.model.common.Link;

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
public class Project extends BaseModel {

    private String name;
    private String description;
    private String techStack;
    private String link;
    private String github;
    private List<Link> images;

    @DynamoDbPartitionKey
    public String getType() {
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }

    @DynamoDbAttribute("tech_stack")
    public String getTechStack() {
        return techStack;
    }
}
