package com.food.auth.dto;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String otp;
}