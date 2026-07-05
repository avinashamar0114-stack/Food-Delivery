package com.food.admin.service;

import com.food.admin.dto.OrderDTO;
import com.food.admin.dto.PaymentDTO;
import com.food.admin.dto.RestaurantDTO;
import com.food.admin.feign.AuthClient;
import com.food.admin.feign.OrderClient;
import com.food.admin.feign.PaymentClient;
import com.food.admin.feign.RestaurantClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private RestaurantClient restaurantClient;

    @Mock
    private OrderClient orderClient;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private AdminService service;

    // =========================
    // DASHBOARD STATS
    // =========================

    @Test
    void dashboardStats() {

        RestaurantDTO restaurant =
                new RestaurantDTO();

        OrderDTO order =
                new OrderDTO();

        order.setStatus("PAID");

        PaymentDTO payment =
                new PaymentDTO();

        payment.setStatus("SUCCESS");

        payment.setAmount(1000.0);

        payment.setMethod("COD");

        when(authClient.totalUsers())
                .thenReturn(1L);

        when(restaurantClient.all())
                .thenReturn(List.of(restaurant));

        when(orderClient.all())
                .thenReturn(List.of(order));

        when(paymentClient.all())
                .thenReturn(List.of(payment));

        var stats = service.dashboard();

        assertEquals(
                1,
                stats.getTotalUsers()
        );

        assertEquals(
                1,
                stats.getTotalRestaurants()
        );

        assertEquals(
                1,
                stats.getTotalOrders()
        );

        assertEquals(
                1000.0,
                stats.getTotalRevenue()
        );
    }

    // =========================
    // PENDING ORDERS
    // =========================

    @Test
    void pendingOrders() {

        OrderDTO order =
                new OrderDTO();

        order.setStatus("CREATED");

        when(authClient.totalUsers())
                .thenReturn(0L);

        when(restaurantClient.all())
                .thenReturn(List.of());

        when(orderClient.all())
                .thenReturn(List.of(order));

        when(paymentClient.all())
                .thenReturn(List.of());

        var stats = service.dashboard();

        assertEquals(
                1,
                stats.getPendingOrders()
        );
    }

    // =========================
    // CANCELLED ORDERS
    // =========================

    @Test
    void cancelledOrders() {

        OrderDTO order =
                new OrderDTO();

        order.setStatus("CANCELLED");

        when(authClient.totalUsers())
                .thenReturn(0L);

        when(restaurantClient.all())
                .thenReturn(List.of());

        when(orderClient.all())
                .thenReturn(List.of(order));

        when(paymentClient.all())
                .thenReturn(List.of());

        var stats = service.dashboard();

        assertEquals(
                1,
                stats.getCancelledOrders()
        );
    }

    // =========================
    // PAID ORDERS
    // =========================

    @Test
    void paidOrders() {

        OrderDTO order =
                new OrderDTO();

        order.setStatus("PAID");

        when(authClient.totalUsers())
                .thenReturn(0L);

        when(restaurantClient.all())
                .thenReturn(List.of());

        when(orderClient.all())
                .thenReturn(List.of(order));

        when(paymentClient.all())
                .thenReturn(List.of());

        var stats = service.dashboard();

        assertEquals(
                1,
                stats.getPaidOrders()
        );
    }
}