package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;
import com.bhupesh.portfolio.model.common.BaseModel;
import com.bhupesh.portfolio.model.common.Link;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data 
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Detail extends BaseModel {

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 254, message = "Email must be at most 254 characters")
    private String email;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;

    @NotBlank(message = "About is required")
    @Size(max = 10000, message = "About must be at most 10000 characters")
    private String about;

    @Valid
    @Size(max = 100, message = "At most 100 achievements are allowed")
    private List<Achievement> achievements;

    @Valid
    @Size(max = 100, message = "At most 100 links are allowed")
    private List<Link> links;

    @Size(max = 2048, message = "Image key must be at most 2048 characters")
    @NotBlank(message = "Image is required")
    private String image;

    @Size(max = 2048, message = "Resume key must be at most 2048 characters")
    @NotBlank(message = "Resume is required")
    private String resume;

    @Size(max = 2048, message = "Logo key must be at most 2048 characters")
    @NotBlank(message = "Logo is required")
    private String logo;

    @DynamoDbPartitionKey
    public String getType() {
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }
}
