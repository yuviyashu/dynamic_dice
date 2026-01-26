package com.dynamic.dice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private Instant timestamp;
    private String path;
}
