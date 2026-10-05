package com.bhupesh.portfolio.model;

import java.util.List;

import com.bhupesh.portfolio.model.common.Achievement;
import com.bhupesh.portfolio.model.common.BaseModel;
import com.bhupesh.portfolio.model.common.Link;

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
public class Detail extends BaseModel {
    
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
        return super.getType();
    }

    @DynamoDbSortKey
    public String getId() {
        return super.getId();
    }
}
