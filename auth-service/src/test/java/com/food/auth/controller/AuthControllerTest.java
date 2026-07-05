package com.food.auth.controller;

import com.food.auth.dto.LoginRequest;
import com.food.auth.dto.RegisterRequest;
import com.food.auth.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService service;

    @InjectMocks
    private AuthController controller;

    @Test
    void register() {

        RegisterRequest request =
                new RegisterRequest();

        request.setEmail("user@gmail.com");
        request.setPassword("1234");
        request.setRole("USER");

        when(service.register(anyString(),
                anyString(),
                anyString()))
                .thenReturn("OTP Sent");

        assertNotNull(
                controller.register(request)
        );
    }

    @Test
    void login() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail("user@gmail.com");
        request.setPassword("1234");

        when(service.login(anyString(), anyString()))
                .thenReturn("TOKEN");

        when(service.getRole(anyString()))
                .thenReturn("USER");

        Map<String, String> response =
                controller.login(request);

        assertEquals("TOKEN",
                response.get("token"));
    }
}