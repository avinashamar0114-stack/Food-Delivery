package com.food.gateway.security;

import io.jsonwebtoken.Claims;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwt =
            new JwtUtil();

    @Test
    void generateToken() {

        String token =
                jwt.generateToken(
                        "user@gmail.com",
                        "USER"
                );

        assertNotNull(token);
    }

    @Test
    void validateToken() {

        String token =
                jwt.generateToken(
                        "user@gmail.com",
                        "USER"
                );

        assertTrue(jwt.isValid(token));
    }

    @Test
    void invalidToken() {

        assertFalse(
                jwt.isValid("INVALID")
        );
    }

    @Test
    void extractClaims() {

        String token =
                jwt.generateToken(
                        "admin@gmail.com",
                        "ADMIN"
                );

        Claims claims =
                jwt.getClaims(token);

        assertEquals(
                "ADMIN",
                claims.get("role")
        );

        assertEquals(
                "admin@gmail.com",
                claims.getSubject()
        );
    }
}