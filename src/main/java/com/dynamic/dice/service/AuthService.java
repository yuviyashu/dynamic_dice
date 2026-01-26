package com.dynamic.dice.service;

import com.dynamic.dice.dto.AdminOtpVerifyRequest;
import com.dynamic.dice.dto.LoginRequest;
import com.dynamic.dice.dto.LoginResponse;
import com.dynamic.dice.dto.UserOtpVerifyRequest;

public interface AuthService {

    // Admin
    void requestAdminOtp(String email);
    void verifyAdminOtpAndRegister(AdminOtpVerifyRequest request);

    // User
    void requestUserOtp(String userEmail, String adminEmail);
    void verifyUserOtpAndRegister(UserOtpVerifyRequest request);

    LoginResponse login(LoginRequest request);
}

