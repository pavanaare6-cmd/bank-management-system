package com.bank.exception;

/**
 * Thrown for business logic errors in the banking system.
 * Examples:
 * - Insufficient funds during withdrawal
 * - Email already registered
 * - Cannot transfer to your own account
 */
public class BankException extends RuntimeException {
    public BankException(String message) {
        super(message);
    }
}
