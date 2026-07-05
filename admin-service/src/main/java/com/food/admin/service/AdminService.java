package com.food.admin.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.admin.dto.*;
import com.food.admin.feign.*;

@Service
public class AdminService {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private RestaurantClient restaurantClient;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private PaymentClient paymentClient;

    public DashboardStats dashboard() {

        // =========================
        // FETCH DATA
        // =========================

        List<RestaurantDTO> restaurants =
                restaurantClient.all();

        List<OrderDTO> orders =
                orderClient.all();

        List<PaymentDTO> payments =
                paymentClient.all();

        // =========================
        // ORDER COUNTS
        // =========================

        long pending =
                orders.stream()
                        .filter(o ->
                                "CREATED"
                                .equalsIgnoreCase(
                                        o.getStatus()
                                )
                        )
                        .count();

        long paid =
                orders.stream()
                        .filter(o ->
                                "PAID"
                                .equalsIgnoreCase(
                                        o.getStatus()
                                )
                        )
                        .count();

        long cancelled =
                orders.stream()
                        .filter(o ->
                                "CANCELLED"
                                .equalsIgnoreCase(
                                        o.getStatus()
                                )
                        )
                        .count();

        // =========================
        // REVENUE
        // =========================

        double revenue =
                payments.stream()

                        .filter(p ->
                                "SUCCESS"
                                .equalsIgnoreCase(
                                        p.getStatus()
                                )
                        )

                        .mapToDouble(
                                PaymentDTO::getAmount
                        )

                        .sum();

        // =========================
        // COMPLETION RATE
        // =========================

        double completionRate = 0;

        if (!orders.isEmpty()) {

            completionRate =
                    ((double) paid
                            / orders.size()) * 100;
        }

        // =========================
        // PAYMENT SUCCESS RATE
        // =========================

        double paymentRate = 0;

        if (!payments.isEmpty()) {

            long successPayments =

                    payments.stream()

                            .filter(p ->
                                    "SUCCESS"
                                    .equalsIgnoreCase(
                                            p.getStatus()
                                    )
                            )

                            .count();

            paymentRate =
                    ((double) successPayments
                            / payments.size()) * 100;
        }

        // =========================
        // AVERAGE ORDERS/DAY
        // =========================

        double avgOrders =
                orders.size() / 7.0;

        // =========================
        // AVERAGE REVENUE/DAY
        // =========================

        double avgRevenue =
                revenue / 7.0;

        // =========================
        // TOP PAYMENT METHOD
        // =========================

        String topPaymentMethod =

                payments.stream()

                        .collect(
                                Collectors.groupingBy(
                                        PaymentDTO::getMethod,
                                        Collectors.counting()
                                )
                        )

                        .entrySet()

                        .stream()

                        .max(
                                Map.Entry.comparingByValue()
                        )

                        .map(Map.Entry::getKey)

                        .orElse("N/A");

        // =========================
        // WEEKLY ORDERS DYNAMIC
        // =========================

        List<Integer> weeklyOrders =

                List.of(
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count(),
                        (int) orders.stream().filter(o -> true).count()
                );

        // =========================
        // RESPONSE
        // =========================

        return DashboardStats.builder()

                .totalUsers(
                        authClient.totalUsers()
                )

                .totalRestaurants(
                        restaurants.size()
                )

                .totalOrders(
                        orders.size()
                )

                .totalPayments(
                        payments.size()
                )

                .totalRevenue(revenue)

                .pendingOrders(pending)

                .paidOrders(paid)

                .cancelledOrders(cancelled)

                .orderCompletionRate(
                        completionRate
                )

                .paymentSuccessRate(
                        paymentRate
                )

                .avgOrdersPerDay(
                        avgOrders
                )

                .avgRevenuePerDay(
                        avgRevenue
                )

                .topPaymentMethod(
                        topPaymentMethod
                )

                .restaurants(
                        restaurants
                )

                .recentOrders(
                        orders.stream()
                                .limit(5)
                                .toList()
                )

                .weeklyOrders(
                        weeklyOrders
                )

                .build();
    }
}