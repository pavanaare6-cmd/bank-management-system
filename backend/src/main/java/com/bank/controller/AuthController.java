package com.bank.controller;

import com.bank.dto.ApiResponse;
import com.bank.dto.AuthResponse;
import com.bank.dto.LoginRequest;
import com.bank.dto.RegisterRequest;
import com.bank.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller.
 * Handles user registration and login.
 *
 * Base URL: /api/auth
 * These endpoints are PUBLIC - no JWT token required.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow requests from the frontend
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/register
     * Register a new user account.
     *
     * Request body:
     * {
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "password": "secret123"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Registration successful! Welcome to the bank.", authResponse));
    }

    /**
     * POST /api/auth/login
     * Login with existing credentials.
     *
     * Request body:
     * {
     *   "email": "john@example.com",
     *   "password": "secret123"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(new ApiResponse(true, "Login successful!", authResponse));
    }
}
