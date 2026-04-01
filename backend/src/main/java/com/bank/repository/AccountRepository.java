package com.bank.repository;

import com.bank.model.Account;
import com.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account entity.
 * Provides database operations for bank accounts.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Find all accounts belonging to a specific user
    List<Account> findByUser(User user);

    // Find account by account number (e.g., "ACC1001")
    Optional<Account> findByAccountNumber(String accountNumber);

    // Check if account number already exists
    boolean existsByAccountNumber(String accountNumber);

    // Find accounts by user ID
    List<Account> findByUserId(Long userId);
}
