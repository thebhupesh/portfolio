package com.bhupesh.portfolio.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bhupesh.portfolio.model.ExperienceModel;
import com.bhupesh.portfolio.repository.generic.DynamoDBGenericRepository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Repository 
public class ExperienceRepository extends DynamoDBGenericRepository<ExperienceModel> {

    public ExperienceRepository(DynamoDbEnhancedClient client, @Value("${aws.dynamodb.table}") String tableName) {

        super(client, tableName, "experience", ExperienceModel.class);
    }
}
