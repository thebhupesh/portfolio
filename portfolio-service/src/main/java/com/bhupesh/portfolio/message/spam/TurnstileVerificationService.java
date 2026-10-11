// package com.bhupesh.portfolio.message.spam;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Locale;
// import java.util.Map;
// import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClient;
// import org.springframework.web.client.RestClientException;

// import com.bhupesh.portfolio.message.Message;

// @Service
// public class TurnstileVerificationService {

//     private static final Logger LOGGER = LoggerFactory.getLogger(TurnstileVerificationService.class);

//     private final RestClient restClient;
//     private final String secretKey;
//     private final String siteverifyUrl;
//     private final String expectedAction;
//     private final List<String> allowedHostnames;

//     public TurnstileVerificationService(
//             RestClient.Builder restClientBuilder,
//             @Value("${cloudflare.turnstile.secret-key}") String secretKey,
//             @Value("${cloudflare.turnstile.allowed-hostnames}") String allowedHostnames,
//             @Value("${cloudflare.turnstile.siteverify-url}") String siteverifyUrl,
//             @Value("${cloudflare.turnstile.expected-action}") String expectedAction) {
//         this.restClient = restClientBuilder.build();
//         this.secretKey = secretKey;
//         this.siteverifyUrl = siteverifyUrl;
//         this.expectedAction = expectedAction;
//         this.allowedHostnames = Arrays.stream(allowedHostnames.split(","))
//                 .map(String::trim)
//                 .map(hostname -> hostname.toLowerCase(Locale.ROOT))
//                 .filter(hostname -> !hostname.isEmpty())
//                 .toList();
//     }

//     public List<String> validate(Message message, String clientIp) {
//         Optional<String> token = Optional.ofNullable(message)
//                 .map(Message::getTurnstileToken)
//                 .filter(value -> !value.isBlank());
//         if (token.isEmpty()) {
//             return List.of("CAPTCHA verification is required");
//         }
//         if (secretKey.isBlank()) {
//             return List.of("CAPTCHA verification is not configured");
//         }
//         if (allowedHostnames.isEmpty()) {
//             return List.of("CAPTCHA hostname verification is not configured");
//         }

//         Map<String, String> request = Optional.ofNullable(clientIp)
//                 .filter(ipAddress -> !ipAddress.isBlank())
//                 .map(ipAddress -> Map.of(
//                         "secret", secretKey,
//                         "response", token.orElseThrow(),
//                         "remoteip", ipAddress))
//                 .orElseGet(() -> Map.of("secret", secretKey, "response", token.orElseThrow()));

//         final SiteverifyResponse result;
//         try {
//             result = restClient.post()
//                     .uri(siteverifyUrl)
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .body(request)
//                     .retrieve()
//                     .body(SiteverifyResponse.class);
//         } catch (RestClientException exception) {
//             LOGGER.warn("Turnstile verification service request failed: exceptionType={}",
//                     exception.getClass().getSimpleName());
//             return List.of("CAPTCHA verification is temporarily unavailable");
//         }

//         if (!Optional.ofNullable(result).map(SiteverifyResponse::success).orElse(false)) {
//             return List.of("CAPTCHA verification failed");
//         }
//         SiteverifyResponse verification = Optional.ofNullable(result).orElseThrow();
//         if (!expectedAction.equals(verification.action())) {
//             return List.of("CAPTCHA action did not match this request");
//         }
//         if (!Optional.ofNullable(verification.hostname())
//                 .map(hostname -> allowedHostnames.contains(hostname.toLowerCase(Locale.ROOT)))
//                 .orElse(false)) {
//             return List.of("CAPTCHA hostname did not match this site");
//         }

//         return List.of();
//     }

//     private record SiteverifyResponse(boolean success, String action, String hostname) {
//     }
// }
