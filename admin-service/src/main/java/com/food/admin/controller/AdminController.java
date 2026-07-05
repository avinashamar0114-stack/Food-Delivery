package com.food.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.food.admin.dto.DashboardStats;
import com.food.admin.service.AdminService;

@RestController
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/dashboard")
    public DashboardStats dashboard() {

        return service.dashboard();
    }
}