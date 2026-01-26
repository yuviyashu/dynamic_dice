package com.dynamic.dice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;

    @Override
    public String toString() {
        return "ApiResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}

