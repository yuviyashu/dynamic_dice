package com.dynamic.dice.utils;

import java.security.SecureRandom;

public final class OtpUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil() {
    }

    /** Generates a cryptographically random 6-digit OTP (100000-999999). */
    public static String generateOtp() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }
}
