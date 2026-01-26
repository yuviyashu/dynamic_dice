package com.dynamic.dice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "otp_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {

    @Id
    private String id;

    /**
     * Email to which OTP was sent
     * - Admin email for both Admin & User registration
     */
    @Indexed
    private String email;

    /**
     * 6-digit OTP (store as String to preserve leading zeros if any)
     */
    private String otp;

    /**
     * ADMIN_REGISTRATION | USER_REGISTRATION
     */
    private String purpose;

    /**
     * OTP expiry timestamp
     */
    private Instant expiresAt;

    /**
     * Indicates whether OTP is already used
     */
    private boolean verified;

    /**
     * Extra metadata (example: userEmail for USER_REGISTRATION)
     */
    private Map<String, String> metadata;

    /**
     * Audit
     */
    private Instant createdAt;
}

