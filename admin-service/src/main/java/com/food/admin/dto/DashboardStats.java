package com.food.admin.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStats {

    private long totalUsers;

    private long totalRestaurants;

    private long totalOrders;

    private long totalPayments;

    private double totalRevenue;

    private long pendingOrders;

    private long paidOrders;

    private long cancelledOrders;

    private double orderCompletionRate;

    private double paymentSuccessRate;

    private double avgOrdersPerDay;

    private double avgRevenuePerDay;

    private String topPaymentMethod;

    private List<RestaurantDTO> restaurants;

    private List<OrderDTO> recentOrders;

    private List<Integer> weeklyOrders;
}