package com.bank.service;

import com.bank.dto.AccountResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.exception.BankException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.Account;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Account Service.
 * Handles bank account creation, retrieval, and management.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create a new bank account for a user.
     * Generates a unique account number automatically.
     */
    public AccountResponse createAccount(User user, CreateAccountRequest request) {
        // Generate a unique account number like "ACC" + 7 random digits
        String accountNumber = generateUniqueAccountNumber();

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO); // Start with zero balance
        account.setAccountType(request.getAccountType());

        Account savedAccount = accountRepository.save(account);
        return mapToResponse(savedAccount);
    }

    /**
     * Get all accounts belonging to a user.
     */
    public List<AccountResponse> getUserAccounts(User user) {
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific account by its ID.
     * Verifies the account belongs to the requesting user.
     */
    public AccountResponse getAccountById(@org.springframework.lang.NonNull Long accountId, @org.springframework.lang.NonNull User user) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with ID: " + accountId));

        // Security check: ensure the account belongs to this user
        if (!account.getUser().getId().equals(user.getId())) {
            throw new BankException("Access denied: This account does not belong to you.");
        }

        return mapToResponse(account);
    }

    /**
     * Get the Account entity (used internally by TransactionService).
     */
    public Account getAccountEntity(@org.springframework.lang.NonNull Long accountId, @org.springframework.lang.NonNull User user) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with ID: " + accountId));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new BankException("Access denied: This account does not belong to you.");
        }

        return account;
    }

    /**
     * Generate a unique account number.
     * Format: "ACC" + 7 random digits (e.g., ACC1234567)
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();

        // Keep generating until we find one that doesn't exist
        do {
            int number = 1000000 + random.nextInt(9000000); // 7-digit number
            accountNumber = "ACC" + number;
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    /**
     * Convert Account entity to AccountResponse DTO.
     */
    public AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setAccountType(account.getAccountType());
        response.setOwnerName(account.getUser().getName());
        response.setOwnerEmail(account.getUser().getEmail());
        return response;
    }

    /**
     * Get all accounts (admin only).
     */
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
