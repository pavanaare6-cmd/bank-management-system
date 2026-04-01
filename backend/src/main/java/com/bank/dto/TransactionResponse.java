package com.bank.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Transaction data returned to the frontend */
@Data
public class TransactionResponse {
    private Long transactionId;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime timestamp;
    private String accountNumber;
}
