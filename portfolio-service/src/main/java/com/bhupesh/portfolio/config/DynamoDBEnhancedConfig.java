package com.bhupesh.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration 
public class DynamoDBEnhancedConfig {

    @Value("${aws.region}")
    private String region;

    @Value ("${aws.credentials.id}")
    private String keyId;

    @Value ("${aws.credentials.secret}")
    private String keySecret;

    @Bean
    @Profile("local")
    public DynamoDbClient localDynamoDbClient() {

        return DynamoDbClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(
                        StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(keyId, keySecret)
                        )
                    ).build();
    }

    @Bean
    @Profile("production")
    public DynamoDbClient dynamoDbClient() {

        return DynamoDbClient.builder()
                .region(Region.of(region)).build();
    }
    
    @Bean
    public DynamoDbEnhancedClient enhancedClient(DynamoDbClient client) {

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
    }
}
