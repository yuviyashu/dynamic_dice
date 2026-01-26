package com.dynamic.dice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiceFace {
    private String value;       // 1 to 6
    private String imageUrl; // Stored path or cloud URL
}

