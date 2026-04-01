package com.bank.dto;

import lombok.Data;
import java.math.BigDecimal;

/** Account data returned to the frontend */
@Data
public class AccountResponse {
    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;
    private String ownerName;
    private String ownerEmail;
}
