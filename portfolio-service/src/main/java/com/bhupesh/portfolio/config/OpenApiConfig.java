package com.bhupesh.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration 
public class OpenApiConfig {

    @Value("${springdoc.api-docs.title}")
    private String API_TITLE;

    @Value("${springdoc.api-docs.description}")
    private String API_DESCRIPTION;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version("1.0")
                        .description(API_DESCRIPTION));
    }
}
