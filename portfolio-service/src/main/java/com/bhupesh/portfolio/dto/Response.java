package com.bhupesh.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder 
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T>(Boolean success, String message, Integer count, T data) {
}
