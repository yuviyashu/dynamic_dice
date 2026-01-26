package com.dynamic.dice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.dynamic.dice.model.OtpRequest;

import java.time.Instant;
import java.util.Optional;

public interface OtpRepository extends MongoRepository<OtpRequest, String> {

    /**
     * Find a valid OTP:
     * - matches email, otp, purpose
     * - not expired
     * - not already verified
     */
    @Query("""
        {
          "email": ?0,
          "otp": ?1,
          "purpose": ?2,
          "verified": false,
          "expiresAt": { "$gt": ?3 }
        }
        """)
    Optional<OtpRequest> findValidOtp(
            String email,
            String otp,
            String purpose,
            Instant now
    );
}

