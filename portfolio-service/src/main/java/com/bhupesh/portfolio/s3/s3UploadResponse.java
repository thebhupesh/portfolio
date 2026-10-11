package com.bhupesh.portfolio.s3;

import lombok.Builder;

@Builder
public record s3UploadResponse(String key, String fileName, String contentType, Long size) {}