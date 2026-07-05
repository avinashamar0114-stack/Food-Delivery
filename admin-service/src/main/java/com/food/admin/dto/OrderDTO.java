package com.food.admin.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;

    private Long userId;

    private Long restaurantId;

    private String item;

    private int quantity;

    private Double price;

    private String status;
}