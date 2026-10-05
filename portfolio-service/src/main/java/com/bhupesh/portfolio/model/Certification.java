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
public class Certification extends BaseModel {
    
    private String name;
    private String provider;
    private String issueDate;
    private String expirationDate;
    private String credentialId;
    private String credentialUrl;
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
    public String getCredentialUrl() {
        return credentialUrl;
    }
}
