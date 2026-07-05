package com.food.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service",
                        r -> r.path("/auth/**")
                                .uri("lb://AUTH-SERVICE"))

                .route("user-service",
                        r -> r.path("/users/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://USER-SERVICE"))

                .route("restaurant-service",
                        r -> r.path("/restaurants/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://RESTAURANT-SERVICE"))

                .route("menu-service",
                        r -> r.path("/menu/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://MENU-SERVICE"))

                .route("order-service",
                        r -> r.path("/orders/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ORDER-SERVICE"))

                .route("payment-service",
                        r -> r.path("/payments/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://PAYMENT-SERVICE"))

                .route("delivery-service",
                        r -> r.path("/delivery/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DELIVERY-SERVICE"))

                .route("admin-service",
                        r -> r.path("/admin/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ADMIN-SERVICE"))

                .build();
    }
}