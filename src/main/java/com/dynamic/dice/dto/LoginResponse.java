package com.dynamic.dice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String role;
    private String userId;
    private String adminId; // null for Admin
}

