package com.bank.repository;

import com.bank.model.Account;
import com.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Transaction entity.
 * Provides database operations for financial transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Get all transactions for a specific account, newest first
    List<Transaction> findByAccountOrderByTimestampDesc(Account account);

    // Get all transactions for a specific account ID, newest first
    List<Transaction> findByAccountAccountIdOrderByTimestampDesc(Long accountId);
}
