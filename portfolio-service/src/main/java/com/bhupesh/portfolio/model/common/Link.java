package com.bhupesh.portfolio.model.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.bhupesh.portfolio.validation.HttpUrl;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Link {

    @NotBlank(message = "Link name is required")
    @Size(max = 200, message = "Link name must be at most 200 characters")
    private String name;

    @NotBlank(message = "Link URL is required")
    @Size(max = 2048, message = "Link URL must be at most 2048 characters")
    @HttpUrl
    private String url;
}
