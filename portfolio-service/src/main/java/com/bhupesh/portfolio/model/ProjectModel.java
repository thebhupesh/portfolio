package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Link;

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
public class ProjectModel {
    
    private String type;
    private String id;
    private String name;
    private String description;
    private String techStack;
    private String link;
    private String github;
    private List<Link> images;

    @DynamoDbPartitionKey
    public String getType() {
        return type;
    }

    @DynamoDbSortKey
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("tech_stack")
    public String getTechStack() {
        return techStack;
    }
}
