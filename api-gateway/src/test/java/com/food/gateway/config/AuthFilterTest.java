package com.food.gateway.config;

import com.food.gateway.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;

import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private JwtUtil jwt;

    @Mock
    private GatewayFilterChain chain;

    @InjectMocks
    private AuthFilter filter;

    private String token;

    @BeforeEach
    void setup() {

        token = "TOKEN";
    }

    @Test
    void publicApiAllowed() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .get("/auth/login")
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        when(chain.filter(any()))
        .thenReturn(Mono.empty());

        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }

    @Test
    void missingHeaderUnauthorized() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .get("/menu")
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }

    @Test
    void invalidToken() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .get("/menu")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        )
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        when(jwt.isValid(token))
                .thenReturn(false);

        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }

    @Test
    void validTokenAllowed() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .get("/menu")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        )
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        when(jwt.isValid(token))
                .thenReturn(true);

        io.jsonwebtoken.Claims claims =
                io.jsonwebtoken.Jwts.claims();

        claims.put("role", "USER");

        when(jwt.getClaims(token))
                .thenReturn(claims);

        when(chain.filter(exchange))
                .thenReturn(Mono.empty());

        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }

    @Test
    void adminBlockedForUser() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .get("/admin/dashboard")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        )
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        when(jwt.isValid(token))
                .thenReturn(true);

        io.jsonwebtoken.Claims claims =
                io.jsonwebtoken.Jwts.claims();

        claims.put("role", "USER");

        when(jwt.getClaims(token))
                .thenReturn(claims);
        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }

    @Test
    void restaurantAllowedToAddMenu() {

        MockServerHttpRequest request =
                MockServerHttpRequest
                        .post("/menu/add")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        )
                        .build();

        ServerWebExchange exchange =
                MockServerWebExchange
                        .from(request);

        when(jwt.isValid(token))
                .thenReturn(true);

        io.jsonwebtoken.Claims claims =
                io.jsonwebtoken.Jwts.claims();

        claims.put("role", "RESTAURANT");

        when(jwt.getClaims(token))
                .thenReturn(claims);

        when(chain.filter(any()))
        .thenReturn(Mono.empty());

        Mono<Void> result =
                filter.filter(exchange, chain);

        assertNotNull(result);
    }
}