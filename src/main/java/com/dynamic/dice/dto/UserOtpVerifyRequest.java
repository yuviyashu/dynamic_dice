package com.dynamic.dice.dto;

import lombok.Data;

@Data
public class UserOtpVerifyRequest {
    private String adminEmail;
    private String otp;
    private String userPassword;
}

