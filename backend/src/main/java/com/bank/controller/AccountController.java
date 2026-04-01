package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.dto.ApiResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.model.User;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Account Controller.
 * Handles bank account creation and management.
 *
 * Base URL: /api/accounts
 * All endpoints REQUIRE a valid JWT token (Authorization: Bearer <token>)
 */
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    /**
     * POST /api/accounts
     * Create a new bank account for the logged-in user.
     *
     * Request body: { "accountType": "SAVINGS" }
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            Authentication authentication) {

        // Get the currently logged-in user from JWT token
        User currentUser = authService.getCurrentUser(authentication.getName());

        AccountResponse account = accountService.createAccount(currentUser, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Account created successfully!", account));
    }

    /**
     * GET /api/accounts
     * Get all accounts belonging to the logged-in user.
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getMyAccounts(Authentication authentication) {
        User currentUser = authService.getCurrentUser(authentication.getName());
        List<AccountResponse> accounts = accountService.getUserAccounts(currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Accounts retrieved successfully", accounts));
    }

    /**
     * GET /api/accounts/{accountId}
     * Get details of a specific account (must belong to logged-in user).
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse> getAccount(
            @PathVariable Long accountId,
            Authentication authentication) {

        User currentUser = authService.getCurrentUser(authentication.getName());
        AccountResponse account = accountService.getAccountById(accountId, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Account details retrieved", account));
    }
}
