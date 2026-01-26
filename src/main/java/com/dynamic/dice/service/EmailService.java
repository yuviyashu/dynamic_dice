package com.dynamic.dice.service;

public interface EmailService {
    void sendOtp(String toEmail, String otp, String message);
}

