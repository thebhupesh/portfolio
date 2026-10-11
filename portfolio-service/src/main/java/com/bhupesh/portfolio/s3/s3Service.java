package com.bhupesh.portfolio.s3;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service 
public class s3Service {

    private final String bucket;
    private final S3Client s3Client;

    public s3Service(@Value("${aws.s3.bucket-name}") String bucket, S3Client s3Client) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public s3UploadResponse upload(MultipartFile file, String type) throws IOException {
        Objects.requireNonNull(file, "file is required");
        String original = Optional.ofNullable(file.getOriginalFilename()).orElse("");
        String extension = Optional.of(original)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse("");

        String key = type + "/" + UUID.randomUUID() + extension;
        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key);
        Optional.ofNullable(file.getContentType()).ifPresent(requestBuilder::contentType);
        PutObjectRequest request = requestBuilder.build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        return s3UploadResponse.builder()
                .key(key)
                .fileName(original)
                .contentType(Optional.ofNullable(file.getContentType()).orElse(""))
                .size(file.getSize())
                .build();
    }

    public ResponseInputStream<GetObjectResponse> download(String type, String key) {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(type + "/" + key)
                        .build()
        );
    }

    public void delete(String type, String key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(type + "/" + key)
                        .build()
        );
    }
}
