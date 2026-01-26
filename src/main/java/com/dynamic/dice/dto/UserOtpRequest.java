package com.dynamic.dice.dto;

import lombok.Data;

@Data
public class UserOtpRequest {
    private String userEmail;
    private String adminEmail;
}

