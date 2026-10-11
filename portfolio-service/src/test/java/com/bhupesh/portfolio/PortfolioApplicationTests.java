package com.bhupesh.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"aws.region=us-east-1",
		"aws.dynamodb.table-name=portfolio-test",
		"aws.sqs.message-queue-url=https://sqs.us-east-1.amazonaws.com/123456789012/portfolio-test",
		"aws.s3.bucket-name=portfolio-test",
		"security.bcrypt-strength=4",
		"security.jwt.signing-key-base64=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=",
		"security.jwt.ttl=PT15M",
		"security.cors.allowed-origins=",
		"server.servlet.context-path=/test",
		"springdoc.api-docs.title=Portfolio API",
		"springdoc.api-docs.description=Portfolio API test context"
})
class PortfolioApplicationTests {

	@Test
	void contextLoads() {
	}

}
