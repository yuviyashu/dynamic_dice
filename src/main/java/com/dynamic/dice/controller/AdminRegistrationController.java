package com.dynamic.dice.controller;

import com.dynamic.dice.dto.AdminOtpVerifyRequest;
import com.dynamic.dice.dto.ApiResponse;
import com.dynamic.dice.dto.EmailRequest;
import com.dynamic.dice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin/register")
@RequiredArgsConstructor
public class AdminRegistrationController {

    private final AuthService authService;

    /**
     * Step 1: Request OTP for Admin registration
     */
    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestAdminOtp(
            @RequestBody EmailRequest request) {

        authService.requestAdminOtp(request.getEmail());

        return ResponseEntity.ok(
                new ApiResponse("OTP sent to admin email successfully")
        );
    }

    /**
     * Step 2: Verify OTP + create Admin account
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyAdminOtp(
            @RequestBody AdminOtpVerifyRequest request) {

        authService.verifyAdminOtpAndRegister(request);

        return ResponseEntity.ok(
                new ApiResponse("Admin registered successfully")
        );
    }


}

