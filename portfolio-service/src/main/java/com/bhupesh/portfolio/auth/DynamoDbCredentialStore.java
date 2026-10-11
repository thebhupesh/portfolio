package com.bhupesh.portfolio.auth;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

@Repository
public class DynamoDbCredentialStore {

    private static final String TYPE_KEY = "type";
    private static final String ID_KEY = "id";
    private static final String PASSWORD_HASH_ATTRIBUTE = "password_hash";
    private static final String ROLE_ATTRIBUTE = "role";

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;
    private final String credentialRecordType;

    public DynamoDbCredentialStore(
            DynamoDbClient dynamoDbClient,
            @Value("${aws.dynamodb.table-name}") String tableName,
            @Value("${security.credentials.record-type}") String credentialRecordType) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
        this.credentialRecordType = credentialRecordType;
    }

    public Optional<StoredCredential> findByUsername(String username) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .consistentRead(true)
                .key(Map.of(
                        TYPE_KEY, AttributeValue.builder().s(credentialRecordType).build(),
                        ID_KEY, AttributeValue.builder().s(username).build()))
                .build();

        return Optional.ofNullable(dynamoDbClient.getItem(request).item())
                .filter(item -> !item.isEmpty())
                .map(item -> new StoredCredential(
                        Optional.ofNullable(item.get(PASSWORD_HASH_ATTRIBUTE))
                                .map(AttributeValue::s)
                                .orElse(""),
                        Optional.ofNullable(item.get(ROLE_ATTRIBUTE))
                                .map(AttributeValue::s)
                                .orElse("")));
    }

    public record StoredCredential(String passwordHash, String role) {
    }
}
