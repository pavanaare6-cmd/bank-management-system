package com.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Account entity - maps to the 'accounts' table in MySQL.
 * Represents a bank account belonging to a user.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    // Many accounts can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Account number (e.g., "ACC1001") - unique per account
    @Column(unique = true, nullable = false)
    private String accountNumber;

    // Balance uses BigDecimal for precise financial calculations (never use float/double for money!)
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    // Account type: SAVINGS or CURRENT
    @Column(nullable = false)
    private String accountType = "SAVINGS";

    // One account can have many transactions
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
