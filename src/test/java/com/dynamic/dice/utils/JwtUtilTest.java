package com.dynamic.dice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private static final String SECRET = "dGVzdC1vbmx5LWtleS1kby1ub3QtdXNlLWluLXByb2R1Y3Rpb24h";
    private static final String OTHER_SECRET = "YW5vdGhlci10ZXN0LWtleS10aGF0LWlzLWxvbmctZW5vdWdoIQ==";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = newJwtUtil(SECRET, 60_000);
    }

    @Test
    void generatedTokenRoundTripsClaims() {
        String token = jwtUtil.generateToken("user-1", "user@example.com", "USER", "admin-1");

        Claims claims = jwtUtil.extractClaims(token);

        assertThat(claims.getSubject()).isEqualTo("user-1");
        assertThat(claims.get("username", String.class)).isEqualTo("user@example.com");
        assertThat(claims.get("role", String.class)).isEqualTo("USER");
        assertThat(claims.get("adminId", String.class)).isEqualTo("admin-1");
    }

    @Test
    void tokenSignedWithDifferentKeyIsRejected() {
        String foreignToken = newJwtUtil(OTHER_SECRET, 60_000)
                .generateToken("user-1", "user@example.com", "ADMIN", "admin-1");

        assertThatThrownBy(() -> jwtUtil.extractClaims(foreignToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void expiredTokenIsRejected() {
        String expired = newJwtUtil(SECRET, -1_000)
                .generateToken("user-1", "user@example.com", "USER", "admin-1");

        assertThatThrownBy(() -> jwtUtil.extractClaims(expired))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void tokensAreValidAcrossInstancesSharingTheSameSecret() {
        String token = newJwtUtil(SECRET, 60_000)
                .generateToken("user-1", "user@example.com", "USER", "admin-1");

        assertThat(jwtUtil.extractClaims(token).getSubject()).isEqualTo("user-1");
    }

    private static JwtUtil newJwtUtil(String secret, long expirationMs) {
        JwtUtil util = new JwtUtil();
        ReflectionTestUtils.setField(util, "secret", secret);
        ReflectionTestUtils.setField(util, "expirationMs", expirationMs);
        util.init();
        return util;
    }
}
