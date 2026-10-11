package com.bhupesh.portfolio.s3;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bhupesh.portfolio.dto.Response;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/v1/s3")
public class s3Controller {
    
    private final s3Service s3Service;

    public s3Controller(s3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<?>> uploadFile(@RequestParam MultipartFile file, @RequestParam String type)
            throws IOException {
        return ResponseEntity.ok()
                .body(Response.<s3UploadResponse>builder()
                        .success(true)
                        .message("File uploaded successfully")
                        .data(s3Service.upload(file, type))
                .build());
    }

    @GetMapping("/download/{type}/{key}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String type, @PathVariable String key) throws IOException {
        byte[] bytes;
        try (ResponseInputStream<GetObjectResponse> stream = s3Service.download(type, key)) {
            bytes = stream.readAllBytes();
        }
        if (bytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .body(new ByteArrayResource(bytes));
    }

    @DeleteMapping("/delete/{type}/{key}")
    public ResponseEntity<Response<String>> deleteFile(@PathVariable String type, @PathVariable String key) {
        s3Service.delete(type, key);

        return ResponseEntity.ok(Response.<String>builder()
                .success(true)
                .message("File deleted successfully")
                .build());
    }
}
