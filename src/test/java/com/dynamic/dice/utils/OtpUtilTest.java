package com.dynamic.dice.utils;

import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.Assertions.assertThat;

class OtpUtilTest {

    @RepeatedTest(50)
    void otpIsAlwaysSixDigits() {
        assertThat(OtpUtil.generateOtp()).matches("\\d{6}");
    }
}
