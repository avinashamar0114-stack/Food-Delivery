package com.food.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.food.gateway.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements GatewayFilter, Ordered {

    @Autowired
    private JwtUtil jwt;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        String path = exchange
                .getRequest()
                .getURI()
                .getPath();

        // =====================================
        // ALLOW OPTIONS
        // =====================================

        if (exchange.getRequest().getMethod()
                == HttpMethod.OPTIONS) {

            return chain.filter(exchange);
        }

        // =====================================
        // PUBLIC ROUTES
        // =====================================

        if (path.startsWith("/auth") ||
            path.startsWith("/menu") ||
            path.contains("/swagger-ui") ||
            path.contains("/swagger-ui.html") ||
            path.contains("/v3/api-docs") ||
            path.contains("/webjars") ||
            path.contains("/eureka")) {

            return chain.filter(exchange);
        }

        // =====================================
        // GET TOKEN
        // =====================================

        String authHeader = exchange
                .getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null ||
            !authHeader.startsWith("Bearer ")) {

            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN = " + token);

        System.out.println(
                "VALID = " + jwt.isValid(token)
        );

        // =====================================
        // VALIDATE TOKEN
        // =====================================

        if (!jwt.isValid(token)) {

            return unauthorized(exchange);
        }

        // =====================================
        // ROLE
        // =====================================

        String role = jwt
                .getClaims(token)
                .get("role")
                .toString();

        System.out.println("ROLE = " + role);

        // =====================================
        // ADMIN ONLY
        // =====================================

        if (path.contains("/admin") &&
                !role.equalsIgnoreCase("ADMIN")) {

            return forbidden(exchange);
        }

        if (path.contains("/orders/all") &&
                !role.equalsIgnoreCase("ADMIN") &&
                !role.equalsIgnoreCase("RESTAURANT")) {

            return forbidden(exchange);
        }

        // =====================================
        // RESTAURANT ONLY
        // =====================================

        if (path.contains("/menu/add") &&
                !role.equalsIgnoreCase("RESTAURANT")) {

            return forbidden(exchange);
        }

        // =====================================
        // ALLOW REQUEST
        // =====================================

        return chain.filter(exchange);
    }

    // =====================================
    // 401
    // =====================================

    private Mono<Void> unauthorized(
            ServerWebExchange exchange
    ) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse()
                .setComplete();
    }

    // =====================================
    // 403
    // =====================================

    private Mono<Void> forbidden(
            ServerWebExchange exchange
    ) {

        exchange.getResponse()
                .setStatusCode(HttpStatus.FORBIDDEN);

        return exchange.getResponse()
                .setComplete();
    }

    @Override
    public int getOrder() {

        return -1;
    }
}