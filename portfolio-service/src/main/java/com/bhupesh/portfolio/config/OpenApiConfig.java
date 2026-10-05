package com.bhupesh.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration 
public class OpenAPIConfig {

    private final String apiTitle;
    private final String apiDescription;
    private final String apiVersion;

    public OpenAPIConfig(@Value("${springdoc.api-docs.title}") String apiTitle, @Value("${springdoc.api-docs.description}") String apiDescription) {
        this.apiTitle = apiTitle;
        this.apiDescription = apiDescription;
        this.apiVersion = "1.0";
    }
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .version(apiVersion)
                        .description(apiDescription));
    }
}
