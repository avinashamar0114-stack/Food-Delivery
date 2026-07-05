package com.food.auth.service;

import com.food.auth.entity.AuthUser;
import com.food.auth.repository.AuthRepository;
import com.food.auth.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository repo;

    @Mock
    private JwtUtil jwt;

    @InjectMocks
    private AuthService service;

    private AuthUser user;

    @BeforeEach
    void setup() {

        user = new AuthUser();

        user.setEmail("user@gmail.com");
        user.setPassword("1234");
        user.setRole("USER");
        user.setVerified(true);
    }

    @Test
    void registerSuccess() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.empty());

        String result = service.register(
                "user@gmail.com",
                "1234",
                "USER"
        );

        assertEquals("OTP Sent", result);
    }

    @Test
    void duplicateEmail() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {

            service.register(
                    "user@gmail.com",
                    "1234",
                    "USER"
            );
        });
    }

    @Test
    void invalidRole() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {

            service.register(
                    "user@gmail.com",
                    "1234",
                    "ABC"
            );
        });
    }

    @Test
    void adminRegistrationBlocked() {

        when(repo.findByEmail("admin@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {

            service.register(
                    "admin@gmail.com",
                    "1234",
                    "ADMIN"
            );
        });
    }

    @Test
    void loginSuccess() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        when(jwt.generateToken(anyString(), anyString()))
                .thenReturn("TOKEN");

        String token =
                service.login(
                        "user@gmail.com",
                        "1234"
                );

        assertEquals("TOKEN", token);
    }

    @Test
    void loginWrongPassword() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {

            service.login(
                    "user@gmail.com",
                    "wrong"
            );
        });
    }

    @Test
    void loginUserNotFound() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {

            service.login(
                    "user@gmail.com",
                    "1234"
            );
        });
    }

    @Test
    void loginNotVerified() {

        user.setVerified(false);

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {

            service.login(
                    "user@gmail.com",
                    "1234"
            );
        });
    }

    @Test
    void adminLogin() {

        when(jwt.generateToken(anyString(), anyString()))
                .thenReturn("ADMIN_TOKEN");

        String token =
                service.login(
                        "admin@gmail.com",
                        "admin123"
                );

        assertEquals("ADMIN_TOKEN", token);
    }

    @Test
    void getRoleAdmin() {

        String role =
                service.getRole("admin@gmail.com");

        assertEquals("ADMIN", role);
    }

    @Test
    void getRoleUser() {

        when(repo.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        String role =
                service.getRole("user@gmail.com");

        assertEquals("USER", role);
    }
}