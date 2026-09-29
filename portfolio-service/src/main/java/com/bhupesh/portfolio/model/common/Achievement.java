package com.bhupesh.portfolio.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Achievement {

    String name;
    String date;
}
