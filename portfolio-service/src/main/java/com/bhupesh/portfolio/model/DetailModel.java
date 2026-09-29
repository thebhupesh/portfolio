package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;
import com.bhupesh.portfolio.model.common.Link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class DetailModel {
    
    private String type;
    private String id;
    private String name;
    private String email;
    private String location;
    private String about;
    private List<Achievement> achievements;
    private List<Link> links;
    private String image;
    private String resume;
    private String logo;

    @DynamoDbPartitionKey
    public String getType() {
        return type;
    }

    @DynamoDbSortKey
    public String getId() {
        return id;
    }
}
