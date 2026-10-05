package com.bhupesh.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Response<T> {
    
    Boolean success;
    String message;
    Integer count;
    T data;
}
