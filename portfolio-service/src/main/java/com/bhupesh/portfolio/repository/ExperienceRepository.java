package com.bhupesh.portfolio.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bhupesh.portfolio.model.Experience;
import com.bhupesh.portfolio.repository.generic.DynamoDBGenericRepository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Repository 
public class ExperienceRepository extends DynamoDBGenericRepository<Experience> {

    private static final String EXPERIENCE_RECORD_TYPE = "experience";

    public ExperienceRepository(DynamoDbEnhancedClient client, @Value("${aws.dynamodb.table-name}") String tableName) {
        super(client, tableName, EXPERIENCE_RECORD_TYPE, Experience.class);
    }
}
