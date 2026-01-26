package com.dynamic.dice.dto;

import lombok.Data;

@Data
public class AdminOtpVerifyRequest {
    private String email;
    private String otp;
    private String password;
}

