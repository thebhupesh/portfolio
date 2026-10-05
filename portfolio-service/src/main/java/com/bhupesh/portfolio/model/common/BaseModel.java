package com.bhupesh.portfolio.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class BaseModel {
    
    private String type;
    private String id;
}
