package com.bank.controller;

import com.bank.dto.ApiResponse;
import com.bank.dto.TransactionRequest;
import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;
import com.bank.model.User;
import com.bank.service.AuthService;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Transaction Controller.
 * Handles all financial operations: deposit, withdraw, transfer, history.
 *
 * Base URL: /api/transactions
 * All endpoints REQUIRE a valid JWT token.
 */
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AuthService authService;

    /**
     * POST /api/transactions/deposit/{accountId}
     * Deposit money into the specified account.
     *
     * Request body: { "amount": 1000.00, "description": "Salary" }
     */
    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<ApiResponse> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        User currentUser = authService.getCurrentUser(authentication.getName());
        TransactionResponse txn = transactionService.deposit(accountId, request, currentUser);
        return ResponseEntity.ok(
                new ApiResponse(true, "Deposit of ₹" + request.getAmount() + " successful!", txn));
    }

    /**
     * POST /api/transactions/withdraw/{accountId}
     * Withdraw money from the specified account.
     *
     * Request body: { "amount": 500.00, "description": "ATM withdrawal" }
     */
    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<ApiResponse> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        User currentUser = authService.getCurrentUser(authentication.getName());
        TransactionResponse txn = transactionService.withdraw(accountId, request, currentUser);
        return ResponseEntity.ok(
                new ApiResponse(true, "Withdrawal of ₹" + request.getAmount() + " successful!", txn));
    }

    /**
     * POST /api/transactions/transfer/{fromAccountId}
     * Transfer money from one account to another.
     *
     * Request body:
     * {
     *   "targetAccountNumber": "ACC1234567",
     *   "amount": 200.00,
     *   "description": "Rent payment"
     * }
     */
    @PostMapping("/transfer/{fromAccountId}")
    public ResponseEntity<ApiResponse> transfer(
            @PathVariable Long fromAccountId,
            @Valid @RequestBody TransferRequest request,
            Authentication authentication) {

        User currentUser = authService.getCurrentUser(authentication.getName());
        TransactionResponse txn = transactionService.transfer(fromAccountId, request, currentUser);
        return ResponseEntity.ok(
                new ApiResponse(true, "Transfer of ₹" + request.getAmount() + " successful!", txn));
    }

    /**
     * GET /api/transactions/history/{accountId}
     * Get transaction history for the specified account.
     */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<ApiResponse> getHistory(
            @PathVariable Long accountId,
            Authentication authentication) {

        User currentUser = authService.getCurrentUser(authentication.getName());
        List<TransactionResponse> history =
                transactionService.getTransactionHistory(accountId, currentUser);
        return ResponseEntity.ok(
                new ApiResponse(true, "Transaction history retrieved", history));
    }
}
