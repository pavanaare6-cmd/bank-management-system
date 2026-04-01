package com.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity - maps to the 'transactions' table in MySQL.
 * Records every financial operation (deposit, withdrawal, transfer).
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    // The account this transaction belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Transaction type: DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    @Column(nullable = false)
    private String type;

    // Amount involved in this transaction
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // Description/notes for the transaction
    private String description;

    // Balance after this transaction was completed
    @Column(precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    // Automatically set when the transaction is created
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
