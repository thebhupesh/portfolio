package com.bhupesh.portfolio.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bhupesh.portfolio.model.Certification;
import com.bhupesh.portfolio.repository.generic.DynamoDBGenericRepository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Repository 
public class CertificationRepository extends DynamoDBGenericRepository<Certification> {

    public CertificationRepository(DynamoDbEnhancedClient client, @Value("${aws.dynamodb.table-name}") String tableName) {

        super(client, tableName, "certification", Certification.class);
    }
}