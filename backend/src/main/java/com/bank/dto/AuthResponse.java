package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Response sent to client after successful login/register */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String name;
    private String email;
    private Long userId;
    private String role;

    public AuthResponse(String token, String name, String email, Long userId) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.role = "USER";
    }

    public AuthResponse(String token, String name, String email, Long userId, String role) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.role = role;
    }
}
