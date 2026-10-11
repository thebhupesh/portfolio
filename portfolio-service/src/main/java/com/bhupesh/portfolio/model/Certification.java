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
public class Certification extends BaseModel {

    @NotBlank(message = "Certification name is required")
    @Size(max = 200, message = "Certification name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Certification provider is required")
    @Size(max = 200, message = "Certification provider must be at most 200 characters")
    private String provider;

    @NotBlank(message = "Issue date is required")
    @Size(max = 32, message = "Issue date must be at most 32 characters")
    private String issueDate;

    @Size(max = 32, message = "Expiration date must be at most 32 characters")
    private String expirationDate;

    @Size(max = 200, message = "Credential ID must be at most 200 characters")
    private String credentialId;

    @Valid
    private Link credentialUrl;

    @Valid
    @Size(max = 100, message = "At most 100 documents are allowed")
    private List<Link> documents;

    @DynamoDbPartitionKey
    public String getType() {
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }

    @DynamoDbAttribute("issue_date")
    public String getIssueDate() {
        return issueDate;
    }

    @DynamoDbAttribute("expiration_date")
    public String getExpirationDate() {
        return expirationDate;
    }

    @DynamoDbAttribute("credential_id")
    public String getCredentialId() {
        return credentialId;
    }

    @DynamoDbAttribute("credential_url")
    public Link getCredentialUrl() {
        return credentialUrl;
    }
}
