package com.food.gateway.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "mysecretkeymysecretkeymysecretkey123";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    // =========================================
    // GENERATE TOKEN
    // =========================================

    public String generateToken(
            String email,
            String role
    ) {

        return Jwts.builder()

                .setSubject(email)

                .claim("role", role)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60 * 10
                        )
                )

                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // =========================================
    // VALIDATE TOKEN
    // =========================================

    public boolean isValid(String token) {

        try {

            Jwts.parserBuilder()

                    .setSigningKey(key)

                    .build()

                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            System.out.println(
                    "JWT ERROR = " + e.getMessage()
            );

            return false;
        }
    }

    // =========================================
    // GET CLAIMS
    // =========================================

    public Claims getClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(key)

                .build()

                .parseClaimsJws(token)

                .getBody();
    }
}