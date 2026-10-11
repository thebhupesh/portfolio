# Portfolio Service

Spring Boot REST API for the portfolio application. It serves portfolio data,
stores records in Amazon DynamoDB, stores uploaded assets in Amazon S3, and
queues contact messages in Amazon SQS.

## Technology

- Java 21
- Spring Boot 4
- Spring MVC, validation, and Spring Security
- AWS SDK for Java 2.x (DynamoDB, S3, and SQS)
- Maven

## Requirements

- JDK 21
- Maven 3.9 or newer
- AWS credentials available through the AWS SDK default credentials provider
  chain, with access to the configured DynamoDB table, S3 bucket, and SQS queue

## Run locally

The `local` Spring profile is configured in
[`src/main/resources/application-local.yml`](src/main/resources/application-local.yml).
From this directory, start the service with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The local profile uses port `8080` and context path `/portfolio/api`. For
example, the token endpoint is:

```text
http://localhost:8080/portfolio/api/v1/auth/token
```

Configure AWS credentials separately (for example, using an AWS profile or
environment credentials). Do not commit AWS credentials or use development
configuration values in production.

## Configuration

`application.yml` uses the following environment variables. Except where a
default is shown, provide each value when running without the `local` profile.

| Variable | Required | Description |
| --- | --- | --- |
| `AWS_REGION` | Yes | AWS region used by the SDK clients. |
| `AWS_DYNAMODB_TABLE_NAME` | Yes | DynamoDB table for portfolio records and credentials. |
| `AWS_SQS_MESSAGE_QUEUE_URL` | Yes | Queue URL for submitted contact messages. |
| `AWS_S3_BUCKET_NAME` | Yes | Bucket for portfolio assets. |
| `SECURITY_BCRYPT_STRENGTH` | Yes | Work factor used to verify credential password hashes. |
| `JWT_SIGNING_KEY` | Yes | Base64-encoded HS256 signing key; must decode to at least 32 bytes. |
| `JWT_TOKEN_TTL` | Yes | Token lifetime as a Java duration, such as `PT15M`. |
| `CORS_ALLOWED_ORIGINS` | Yes | Comma-separated exact browser origins; do not use `*` for credentialed clients. |
| `CONTEXT_PATH` | Yes | Servlet context path, such as `/portfolio/api`. |
| `SECURITY_CREDENTIALS_RECORD_TYPE` | No | DynamoDB credential item type; defaults to `credential`. |
| `SPAM_COUNTER_RECORD_TYPE` | No | Configured spam-counter item type; defaults to `spam_counter`. |
| `SPAM_COUNTER_EXPIRATION_GRACE_SECONDS` | No | Counter expiration grace period; defaults to `86400`. |
| `SPAM_IP_MAX_REQUESTS` | No | Configured IP request limit; defaults to `10`. |
| `SPAM_IP_WINDOW_SECONDS` | No | Configured IP limit window; defaults to `3600`. |
| `SPAM_EMAIL_MAX_MESSAGES_PER_WEEK` | No | Configured per-email message limit; defaults to `5`. |
| `SPAM_EMAIL_WINDOW_SECONDS` | No | Configured email limit window; defaults to `604800`. |
| `TURNSTILE_SITEVERIFY_URL` | No | Cloudflare verification URL; defaults to the Cloudflare Siteverify endpoint. |
| `TURNSTILE_SECRET_KEY` | No | Cloudflare Turnstile secret. |
| `TURNSTILE_ALLOWED_HOSTNAMES` | No | Comma-separated allowed Turnstile hostnames. |
| `TURNSTILE_EXPECTED_ACTION` | No | Expected Turnstile action; defaults to `contact`. |

The spam-counter and Turnstile settings are present in configuration, but the
current contact-message flow does not enforce rate limiting or CAPTCHA checks.
Do not assume those protections are active.

The OpenAPI configuration also requires `springdoc.api-docs.title` and
`springdoc.api-docs.description` properties when creating the application
context. The local profile defines them. They can be provided in other
profiles using Spring's environment-variable mapping, for example
`SPRINGDOC_API_DOCS_TITLE` and `SPRINGDOC_API_DOCS_DESCRIPTION`.

Generate a new signing key for each deployment environment; do not reuse a key
that has been committed or exposed:

```bash
openssl rand -base64 32
```

Treat the result as a secret and provide it through a secret manager or a
protected environment variable, not source control.

## Authentication and authorization

`POST /v1/auth/token` is the only unauthenticated application endpoint.
Credentials are accepted using HTTP Basic authentication; JSON login bodies
are not supported:

```bash
curl --user 'reader:your-password' \
  http://localhost:8080/portfolio/api/v1/auth/token
```

The response envelope includes a `data` object containing `accessToken`,
`tokenType` (`Bearer`), and `expiresInSeconds`. Use the token on protected
requests:

```bash
curl http://localhost:8080/portfolio/api/v1/project \
  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'
```

Basic authentication only Base64-encodes credentials; it does not encrypt
them. Use HTTPS outside local development.

Credentials are provisioned in the configured DynamoDB table; this service
does not provide registration or password-reset endpoints. Credential items
use the table's `type` and `id` keys and these attributes:

| Attribute | Value |
| --- | --- |
| `type` | `credential` by default; configurable with `SECURITY_CREDENTIALS_RECORD_TYPE`. |
| `id` | Lowercase username. |
| `password_hash` | BCrypt hash; never store plaintext passwords. |
| `role` | `READ` or `WRITE`. |

BCrypt supports at most 72 UTF-8 bytes per password; longer passwords are
rejected. `READ` credentials can use GET endpoints and validate contact
messages. `WRITE` credentials can additionally create, update, or delete
portfolio records, upload or delete assets, and submit contact messages.

## API reference

All routes below are relative to the configured context path. Requests and
responses use JSON unless indicated otherwise. Protected routes require a
Bearer token.

| Method | Route | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/v1/auth/token` | Public | Exchange HTTP Basic credentials for a JWT. |
| `GET` | `/v1/detail` | `READ` or `WRITE` | Get portfolio owner details. |
| `GET` | `/v1/detail/name` | `READ` or `WRITE` | Get the portfolio owner name. |
| `PUT` | `/v1/detail` | `WRITE` | Update portfolio owner details. |
| `GET` | `/v1/project` | `READ` or `WRITE` | List projects. |
| `GET` | `/v1/project/{id}` | `READ` or `WRITE` | Get a project. |
| `POST` | `/v1/project` | `WRITE` | Create a project. |
| `PUT` | `/v1/project/{id}` | `WRITE` | Update a project. |
| `DELETE` | `/v1/project/{id}` | `WRITE` | Delete a project. |
| `GET` | `/v1/experience` | `READ` or `WRITE` | List experiences. |
| `GET` | `/v1/experience/{id}` | `READ` or `WRITE` | Get an experience. |
| `POST` | `/v1/experience` | `WRITE` | Create an experience. |
| `PUT` | `/v1/experience/{id}` | `WRITE` | Update an experience. |
| `DELETE` | `/v1/experience/{id}` | `WRITE` | Delete an experience. |
| `GET` | `/v1/certification` | `READ` or `WRITE` | List certifications. |
| `GET` | `/v1/certification/{id}` | `READ` or `WRITE` | Get a certification. |
| `POST` | `/v1/certification` | `WRITE` | Create a certification. |
| `PUT` | `/v1/certification/{id}` | `WRITE` | Update a certification. |
| `DELETE` | `/v1/certification/{id}` | `WRITE` | Delete a certification. |
| `POST` | `/v1/message/validate` | `READ` or `WRITE` | Validate a contact message without sending it. |
| `POST` | `/v1/message/send` | `WRITE` | Validate and enqueue a contact message in SQS. |
| `POST` | `/v1/s3/upload` | `WRITE` | Upload a multipart file using `file` and `type` fields. |
| `GET` | `/v1/s3/download/{type}/{key}` | `READ` or `WRITE` | Download an asset. |
| `DELETE` | `/v1/s3/delete/{type}/{key}` | `WRITE` | Delete an asset. |

Create and update routes accept the corresponding model as a JSON object.
Required fields and nested model constraints are declared on the Java models;
invalid requests return `400 Bad Request` with sorted validation messages.
Collection endpoints return `204 No Content` when no records are present.

The local profile configures OpenAPI JSON and Swagger UI paths as `/endpoints`
and `/swagger/endpoints`, respectively, appended to the context path. The
current security rules deny non-`/v1` routes, including these documentation
paths; permit them explicitly in the security configuration if they should be
available.

## Data and integrations

- **DynamoDB:** Stores portfolio data and pre-provisioned authentication
  credentials. Portfolio model items use `type` as the partition key and `id`
  as the sort key.
- **S3:** Stores files. The upload API returns the stored asset information
  used by other portfolio records.
- **SQS:** Receives validated contact-message payloads. A separate queue
  consumer is responsible for processing those messages.
- **CORS:** Allows the configured origins and standard API methods. Only
  `Authorization` and `Content-Type` request headers are allowed.

## Build and test

Run the test suite:

```bash
mvn test
```

Build the executable Spring Boot artifact:

```bash
mvn clean package
```

The packaged JAR is written to `target/portfolio-service-0.0.1-SNAPSHOT.jar`.
Run it with the required production configuration available in the process
environment:

```bash
java -jar target/portfolio-service-0.0.1-SNAPSHOT.jar
```
