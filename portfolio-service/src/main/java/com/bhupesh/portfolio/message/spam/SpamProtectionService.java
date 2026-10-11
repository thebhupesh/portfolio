// package com.bhupesh.portfolio.message.spam;

// import java.time.DayOfWeek;
// import java.time.Instant;
// import java.time.ZoneOffset;
// import java.time.temporal.TemporalAdjusters;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Locale;
// import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import com.bhupesh.portfolio.message.Message;

// @Service
// public class SpamProtectionService {

//     private static final Logger LOGGER = LoggerFactory.getLogger(SpamProtectionService.class);

//     private final DynamoDbRateLimitStore rateLimitStore;
//     private final int maxRequestsPerIp;
//     private final long ipWindowSeconds;
//     private final int maxMessagesPerEmail;
//     private final long emailWindowSeconds;
//     private final long counterExpirationGraceSeconds;

//     public SpamProtectionService(
//             DynamoDbRateLimitStore rateLimitStore,
//             @Value("${spam-protection.ip.max-requests}") int maxRequestsPerIp,
//             @Value("${spam-protection.ip.window-seconds}") long ipWindowSeconds,
//             @Value("${spam-protection.email.max-messages-per-week}") int maxMessagesPerEmail,
//             @Value("${spam-protection.email.window-seconds}") long emailWindowSeconds,
//             @Value("${spam-protection.counter-expiration-grace-seconds}") long counterExpirationGraceSeconds) {
//         this.rateLimitStore = rateLimitStore;
//         this.maxRequestsPerIp = maxRequestsPerIp;
//         this.ipWindowSeconds = ipWindowSeconds;
//         this.maxMessagesPerEmail = maxMessagesPerEmail;
//         this.emailWindowSeconds = emailWindowSeconds;
//         this.counterExpirationGraceSeconds = counterExpirationGraceSeconds;
//         if (maxRequestsPerIp < 1 || maxMessagesPerEmail < 1
//                 || ipWindowSeconds < 1 || emailWindowSeconds < 1
//                 || counterExpirationGraceSeconds < 0) {
//             throw new IllegalArgumentException(
//                     "Spam protection limits and windows must be positive; expiration grace cannot be negative");
//         }
//     }

//     public List<String> validateSubmission(Message message, String clientIp) {
//         List<String> errors = new ArrayList<>();
//         Optional<String> website = Optional.ofNullable(message)
//                 .map(Message::getWebsite)
//                 .filter(value -> !value.isBlank());
//         if (website.isPresent()) {
//             errors.add("Spam check failed");
//         }
//         Optional<String> ipAddress = Optional.ofNullable(clientIp).filter(value -> !value.isBlank());
//         if (ipAddress.isEmpty()) {
//             errors.add("Unable to determine client address");
//         }
//         Optional<String> email = Optional.ofNullable(message)
//                 .map(Message::getSenderEmail)
//                 .filter(value -> !value.isBlank());
//         if (email.isEmpty()) {
//             errors.add("Sender email is required");
//         }
//         if (!errors.isEmpty()) {
//             return List.copyOf(errors);
//         }

//         long now = Instant.now().getEpochSecond();
//         String normalizedIp = ipAddress.orElseThrow();
//         String normalizedEmail = email.orElseThrow().trim().toLowerCase(Locale.ROOT);
//         boolean ipLimitExceeded =
//                 isLimitExceeded("ip:" + normalizedIp, now, ipWindowSeconds, maxRequestsPerIp, false);
//         boolean emailLimitExceeded = isLimitExceeded(
//                 "email:" + normalizedEmail,
//                 now,
//                 emailWindowSeconds,
//                 maxMessagesPerEmail,
//                 true);
//         if (ipLimitExceeded) {
//             LOGGER.warn("Message submission rejected by per-IP rate limit");
//             errors.add("Too many message requests from this address; try again later");
//         }
//         if (emailLimitExceeded) {
//             LOGGER.warn("Message submission rejected by per-email rate limit");
//             errors.add("This email address has reached the weekly message limit");
//         }
//         return List.copyOf(errors);
//     }

//     private boolean isLimitExceeded(
//             String identity, long now, long windowSeconds, int limit, boolean calendarWeek) {
//         long windowStart;
//         if (calendarWeek) {
//             var utcDate = Instant.ofEpochSecond(now).atZone(ZoneOffset.UTC).toLocalDate();
//             windowStart = utcDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
//                     .atStartOfDay(ZoneOffset.UTC)
//                     .toEpochSecond();
//         } else {
//             windowStart = Math.floorDiv(now, windowSeconds) * windowSeconds;
//         }
//         long expiresAt = windowStart + windowSeconds + counterExpirationGraceSeconds;
//         long count = rateLimitStore.increment(identity, windowStart, expiresAt);
//         return count > limit;
//     }
// }
