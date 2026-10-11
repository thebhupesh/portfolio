package com.bhupesh.portfolio.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

class DynamoDbCredentialStoreTest {

    @Test
    void queriesConfiguredCredentialRecordTypeAndMapsCredentialAttributes() {
        DynamoDbClient dynamoDbClient = mock(DynamoDbClient.class);
        when(dynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(GetItemResponse.builder()
                .item(Map.of(
                        "password_hash", AttributeValue.builder().s("$2a$12$hash").build(),
                        "role", AttributeValue.builder().s("READ").build()))
                .build());
        DynamoDbCredentialStore store =
                new DynamoDbCredentialStore(dynamoDbClient, "portfolio", "auth-credential");

        var credential = store.findByUsername("reader").orElseThrow();

        assertEquals("$2a$12$hash", credential.passwordHash());
        assertEquals("READ", credential.role());
        ArgumentCaptor<GetItemRequest> requestCaptor = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(dynamoDbClient).getItem(requestCaptor.capture());
        GetItemRequest request = requestCaptor.getValue();
        assertEquals("portfolio", request.tableName());
        assertEquals(Boolean.TRUE, request.consistentRead());
        assertEquals("auth-credential", request.key().get("type").s());
        assertEquals("reader", request.key().get("id").s());
    }
}
