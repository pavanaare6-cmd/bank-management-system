package com.bank.service;

import com.bank.dto.AuthResponse;
import com.bank.dto.LoginRequest;
import com.bank.dto.RegisterRequest;
import com.bank.exception.BankException;
import com.bank.model.User;
import com.bank.repository.UserRepository;
import com.bank.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Authentication Service.
 * Handles all user registration and login logic.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // BCryptPasswordEncoder

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Register a new user.
     * Steps:
     * 1. Check if email is already taken
     * 2. Hash the password with BCrypt
     * 3. Save the user to the database
     * 4. Generate and return a JWT token
     */
    public AuthResponse register(RegisterRequest request) {
        // Step 1: Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BankException("Email is already registered. Please login instead.");
        }

        // Step 2: Create new user with hashed password
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password!

        // Step 3: Save to database
        User savedUser = userRepository.save(user);

        // Step 4: Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponse(token, savedUser.getName(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole().name());
    }

    /**
     * Login an existing user.
     * Steps:
     * 1. Use Spring Security to authenticate email + password
     * 2. If valid, generate and return a JWT token
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // Step 1: Authenticate using Spring Security (checks email + BCrypt password)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            if (!authentication.isAuthenticated()) {
                throw new BankException("Authentication failed. Please try again.");
            }
        } catch (BadCredentialsException e) {
            throw new BankException("Invalid email or password. Please try again.");
        }

        // Step 2: Load user details and generate token
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BankException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, user.getName(), user.getEmail(), user.getId(), user.getRole().name());
    }

    /**
     * Get the currently logged-in user by their email from the JWT token.
     */
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BankException("User not found"));
    }

    /**
     * Initialize admin user if not exists.
     * Call this on application startup.
     */
    public void initializeAdminUser() {
        String adminEmail = "admin@bank.com";
        String adminPassword = "admin123";
        String adminName = "Bank Admin";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created: " + adminEmail);
        }
    }

    /**
     * Get all users (admin only).
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
