package com.dynamic.dice.dto;

import lombok.Data;

@Data
public class DiceFaceRequest {
    private String value;       // 1 to 6
    private String imageUrl; // uploaded image path or URL
}

