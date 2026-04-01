package com.bank.exception;

/**
 * Thrown when a requested resource is not found.
 * Example: account not found, user not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
