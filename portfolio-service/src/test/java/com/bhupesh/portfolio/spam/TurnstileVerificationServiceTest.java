// package com.bhupesh.portfolio.spam;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
// import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
// import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
// import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.client.MockRestServiceServer;
// import org.springframework.web.client.RestClient;

// import com.bhupesh.portfolio.message.Message;
// import com.bhupesh.portfolio.message.spam.TurnstileVerificationService;

// class TurnstileVerificationServiceTest {

//     private static final String SITEVERIFY_URL =
//             "https://challenges.cloudflare.com/turnstile/v0/siteverify";

//     @Test
//     void acceptsValidTokenForExpectedActionAndHostname() {
//         RestClient.Builder builder = RestClient.builder();
//         MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
//         server.expect(requestTo(SITEVERIFY_URL))
//                 .andExpect(method(HttpMethod.POST))
//                 .andExpect(content().json("""
//                         {"secret":"test-secret","response":"valid-token","remoteip":"192.0.2.1"}
//                         """))
//                 .andRespond(withSuccess("""
//                         {"success":true,"action":"contact","hostname":"portfolio.example"}
//                         """, MediaType.APPLICATION_JSON));
//         TurnstileVerificationService service =
//                 new TurnstileVerificationService(
//                         builder,
//                         "test-secret",
//                         "portfolio.example",
//                         SITEVERIFY_URL,
//                         "contact");

//         assertEquals(List.of(), service.validate(message("valid-token"), "192.0.2.1"));
//         server.verify();
//     }

//     @Test
//     void rejectsInvalidTokenAndUnexpectedAction() {
//         RestClient.Builder builder = RestClient.builder();
//         MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
//         server.expect(requestTo(SITEVERIFY_URL))
//                 .andRespond(withSuccess("""
//                         {"success":false,"error-codes":["invalid-input-response"]}
//                         """, MediaType.APPLICATION_JSON));
//         server.expect(requestTo(SITEVERIFY_URL))
//                 .andRespond(withSuccess("""
//                         {"success":true,"action":"other","hostname":"portfolio.example"}
//                         """, MediaType.APPLICATION_JSON));
//         TurnstileVerificationService service =
//                 new TurnstileVerificationService(
//                         builder,
//                         "test-secret",
//                         "portfolio.example",
//                         SITEVERIFY_URL,
//                         "contact");

//         assertEquals(List.of("CAPTCHA verification failed"),
//                 service.validate(message("invalid-token"), "192.0.2.1"));
//         assertEquals(List.of("CAPTCHA action did not match this request"),
//                 service.validate(message("wrong-action-token"), "192.0.2.1"));
//         server.verify();
//     }

//     @Test
//     void verifiesTheConfiguredActionRatherThanAnInternalDefault() {
//         RestClient.Builder builder = RestClient.builder();
//         MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
//         server.expect(requestTo(SITEVERIFY_URL))
//                 .andRespond(withSuccess("""
//                         {"success":true,"action":"portfolio-message","hostname":"portfolio.example"}
//                         """, MediaType.APPLICATION_JSON));
//         TurnstileVerificationService service =
//                 new TurnstileVerificationService(
//                         builder,
//                         "test-secret",
//                         "portfolio.example",
//                         SITEVERIFY_URL,
//                         "portfolio-message");

//         assertEquals(List.of(), service.validate(message("valid-token"), null));
//         server.verify();
//     }

//     private Message message(String token) {
//         Message message = new Message();
//         message.setTurnstileToken(token);
//         return message;
//     }
// }
