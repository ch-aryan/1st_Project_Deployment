package com.springbootbanking.security;

/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        long expirationMs = 3600000; // 1 hour
        jwtTokenProvider = new JwtTokenProvider(secret, expirationMs);
    }

    @Test
    @DisplayName("Should generate valid token and extract username")
    void testGenerateAndValidateToken() {
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(username, jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("Should fail validation on invalid token")
    void testInvalidToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.jwt.token"));
    }
}
*/
