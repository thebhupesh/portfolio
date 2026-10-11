// package com.bhupesh.portfolio.spam;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.Map;

// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentCaptor;

// import com.bhupesh.portfolio.message.spam.DynamoDbRateLimitStore;

// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
// import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
// import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

// class DynamoDbRateLimitStoreTest {

//     @Test
//     void usesConfiguredCounterRecordType() {
//         DynamoDbClient dynamoDbClient = mock(DynamoDbClient.class);
//         when(dynamoDbClient.updateItem(any(UpdateItemRequest.class)))
//                 .thenReturn(UpdateItemResponse.builder()
//                         .attributes(Map.of(
//                                 "request_count",
//                                 AttributeValue.builder().n("1").build()))
//                         .build());
//         DynamoDbRateLimitStore store =
//                 new DynamoDbRateLimitStore(dynamoDbClient, "portfolio", "custom-spam-counter");

//         assertEquals(1, store.increment("ip:192.0.2.1", 100, 200));

//         ArgumentCaptor<UpdateItemRequest> requestCaptor =
//                 ArgumentCaptor.forClass(UpdateItemRequest.class);
//         verify(dynamoDbClient).updateItem(requestCaptor.capture());
//         assertEquals("custom-spam-counter",
//                 requestCaptor.getValue().key().get("type").s());
//     }
// }
