package com.food.admin.dto;

import lombok.Data;

@Data
public class PaymentDTO {

    private Long id;

    private Long orderId;

    private Double amount;

    private String status;

    private String method;
}