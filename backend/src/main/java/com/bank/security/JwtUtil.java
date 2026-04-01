package com.bank.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT Utility class.
 * Handles creation, validation, and parsing of JWT tokens.
 *
 * How JWT works:
 * 1. User logs in with email + password
 * 2. Server verifies credentials and creates a JWT token containing user info
 * 3. Client stores the token and sends it with every request
 * 4. Server validates the token on each request to identify the user
 */
@Component
public class JwtUtil {

    // Read secret key and expiration from application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate a JWT token for the given email address.
     * The email is stored inside the token as the "subject".
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)                           // Store email in token
                .setIssuedAt(new Date())                     // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Expiry time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    /**
     * Extract the email (subject) from a JWT token.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validate a JWT token - checks signature and expiration.
     * Returns true if token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid (expired, tampered, etc.)
            return false;
        }
    }

    /**
     * Convert the string secret into a cryptographic signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
