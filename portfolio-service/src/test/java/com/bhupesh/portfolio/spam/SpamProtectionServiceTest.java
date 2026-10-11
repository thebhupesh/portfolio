// package com.bhupesh.portfolio.spam;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// import org.junit.jupiter.api.Test;

// import com.bhupesh.portfolio.message.Message;
// import com.bhupesh.portfolio.message.spam.DynamoDbRateLimitStore;
// import com.bhupesh.portfolio.message.spam.SpamProtectionService;

// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
// import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
// import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

// class SpamProtectionServiceTest {

//     @Test
//     void enforcesPerIpAndPerEmailLimits() {
//         DynamoDbRateLimitStore store = inMemoryStore();
//         SpamProtectionService service = new SpamProtectionService(
//                 store, 2, 3600, 1, 604800, 86400);

//         assertTrue(service.validateSubmission(message("person@example.com"), "192.0.2.1").isEmpty());
//         assertEquals(List.of("This email address has reached the weekly message limit"),
//                 service.validateSubmission(message("person@example.com"), "192.0.2.2"));
//         assertEquals(List.of("This email address has reached the weekly message limit"),
//                 service.validateSubmission(message("person@example.com"), "192.0.2.1"));
//         assertEquals(List.of(
//                         "This email address has reached the weekly message limit",
//                         "Too many message requests from this address; try again later"),
//                 service.validateSubmission(message("person@example.com"), "192.0.2.1"));
//     }

//     @Test
//     void rejectsFilledHoneypotWithoutCountingRequest() {
//         DynamoDbClient client = mock(DynamoDbClient.class);
//         DynamoDbRateLimitStore store = new DynamoDbRateLimitStore(client, "test-table", "spam_limit");
//         SpamProtectionService service = new SpamProtectionService(store, 5, 3600, 2, 604800, 86400);
//         Message message = message("person@example.com");
//         message.setWebsite("https://bot.example");

//         assertEquals(List.of("Spam check failed"), service.validateSubmission(message, "192.0.2.1"));
//         verify(client, never()).updateItem(any(UpdateItemRequest.class));
//     }

//     private Message message(String email) {
//         Message message = new Message();
//         message.setSenderEmail(email);
//         return message;
//     }

//     private DynamoDbRateLimitStore inMemoryStore() {
//         DynamoDbClient client = mock(DynamoDbClient.class);
//         Map<String, Long> counts = new ConcurrentHashMap<>();
//         when(client.updateItem(any(UpdateItemRequest.class))).thenAnswer(invocation -> {
//             UpdateItemRequest request = invocation.getArgument(0);
//             String key = request.key().get("id").s();
//             long count = counts.merge(key, 1L, Long::sum);
//             return UpdateItemResponse.builder()
//                     .attributes(Map.of(
//                             "request_count",
//                             AttributeValue.builder().n(Long.toString(count)).build()))
//                     .build();
//         });
//         return new DynamoDbRateLimitStore(client, "test-table", "spam_limit");
//     }
// }
