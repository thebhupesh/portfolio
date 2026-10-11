// package com.bhupesh.portfolio.message.spam;

// import java.nio.charset.StandardCharsets;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.HexFormat;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Repository;

// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
// import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
// import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

// @Repository
// public class DynamoDbRateLimitStore {

//     private final DynamoDbClient dynamoDbClient;
//     private final String tableName;
//     private final String counterRecordType;

//     public DynamoDbRateLimitStore(
//             DynamoDbClient dynamoDbClient,
//             @Value("${aws.dynamodb.table-name}") String tableName,
//             @Value("${spam-protection.counter-record-type}") String counterRecordType) {
//         this.dynamoDbClient = dynamoDbClient;
//         this.tableName = tableName;
//         this.counterRecordType = counterRecordType;
//     }

//     public long increment(String identity, long windowStartEpochSeconds, long expiresAtEpochSeconds) {
//         UpdateItemRequest request = UpdateItemRequest.builder()
//                 .tableName(tableName)
//                 .key(Map.of(
//                         "type", AttributeValue.builder().s(counterRecordType).build(),
//                         "id", AttributeValue.builder()
//                                 .s(hash(identity) + ":" + windowStartEpochSeconds)
//                                 .build()))
//                 .updateExpression("SET #expires_at = :expires_at ADD #request_count :one")
//                 .expressionAttributeNames(Map.of(
//                         "#expires_at", "expires_at",
//                         "#request_count", "request_count"))
//                 .expressionAttributeValues(Map.of(
//                         ":expires_at", AttributeValue.builder().n(Long.toString(expiresAtEpochSeconds)).build(),
//                         ":one", AttributeValue.builder().n("1").build()))
//                 .returnValues(ReturnValue.ALL_NEW)
//                 .build();

//         String count = Optional.ofNullable(dynamoDbClient.updateItem(request).attributes())
//                 .map(attributes -> attributes.get("request_count"))
//                 .map(AttributeValue::n)
//                 .orElseThrow(() -> new IllegalStateException(
//                         "DynamoDB rate-limit update did not return request_count"));
//         return Long.parseLong(count);
//     }

//     private String hash(String value) {
//         try {
//             byte[] digest = MessageDigest.getInstance("SHA-256")
//                     .digest(value.getBytes(StandardCharsets.UTF_8));
//             return HexFormat.of().formatHex(digest);
//         } catch (NoSuchAlgorithmException exception) {
//             throw new IllegalStateException("SHA-256 is unavailable", exception);
//         }
//     }
// }
