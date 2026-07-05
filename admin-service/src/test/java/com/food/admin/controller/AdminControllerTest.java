package com.food.admin.controller;

import com.food.admin.dto.DashboardStats;
import com.food.admin.service.AdminService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService service;

    @InjectMocks
    private AdminController controller;

    @Test
    void dashboard() {

        DashboardStats stats =
                new DashboardStats();

        stats.setTotalUsers(10);

        when(service.dashboard())
                .thenReturn(stats);

        DashboardStats response =
                controller.dashboard();

        assertEquals(10,
                response.getTotalUsers());
    }
}