package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.BaseModel;
import com.bhupesh.portfolio.model.common.Link;
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
public class Project extends BaseModel {

    @NotBlank(message = "Project name is required")
    @Size(max = 200, message = "Project name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Project description is required")
    @Size(max = 10000, message = "Project description must be at most 10000 characters")
    private String description;

    @Size(max = 2000, message = "Tech stack must be at most 2000 characters")
    private String techStack;

    @Valid
    private Link link;

    @Valid
    @Size(max = 100, message = "At most 100 repository links are allowed")
    private List<Link> repositories;

    @Valid
    @Size(max = 100, message = "At most 100 images are allowed")
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
