package com.food.admin.dto;

import lombok.Data;

@Data
public class RestaurantDTO {

    private Long id;

    private String name;

    private String location;

    private String cuisine;

    private Double rating;
}