package com.dynamic.dice.controller;

import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.UserOtpRequest;
import com.dynamic.dice.dto.UserOtpVerifyRequest;
import com.dynamic.dice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user/register")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final AuthService authService;

    /**
     * Step 1: Request OTP for User registration
     * (OTP is sent to Admin email)
     */
    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestUserOtp(
            @RequestBody UserOtpRequest request) {

        authService.requestUserOtp(
                request.getUserEmail(),
                request.getAdminEmail()
        );

        return ResponseEntity.ok(
                new ApiResponse("OTP sent to admin email for user approval")
        );
    }

    /**
     * Step 2: Verify OTP + create User account
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyUserOtp(
            @RequestBody UserOtpVerifyRequest request) {

        authService.verifyUserOtpAndRegister(request);

        return ResponseEntity.ok(
                new ApiResponse("User registered successfully")
        );
    }
}

