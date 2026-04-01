package com.bank.service;

import com.bank.dto.TransactionRequest;
import com.bank.dto.TransactionResponse;
import com.bank.dto.TransferRequest;
import com.bank.exception.BankException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction Service.
 * Handles all financial operations: deposit, withdrawal, transfer.
 *
 * @Transactional ensures database operations are atomic:
 * if one step fails, the entire operation is rolled back.
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Deposit money into an account.
     *
     * @param accountId  The account to deposit into
     * @param request    Contains amount and optional description
     * @param user       The currently logged-in user
     */
    @Transactional
    public TransactionResponse deposit(Long accountId, TransactionRequest request, User user) {
        // Get the account (also verifies ownership)
        Account account = accountService.getAccountEntity(accountId, user);

        // Add the amount to the current balance
        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create a transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("DEPOSIT");
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(request.getDescription() != null
                ? request.getDescription() : "Deposit");
        transaction.setTimestamp(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    /**
     * Withdraw money from an account.
     *
     * @param accountId  The account to withdraw from
     * @param request    Contains amount and optional description
     * @param user       The currently logged-in user
     */
    @Transactional
    public TransactionResponse withdraw(Long accountId, TransactionRequest request, User user) {
        Account account = accountService.getAccountEntity(accountId, user);

        // Check if sufficient funds are available
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BankException(
                    "Insufficient funds. Available balance: ₹" + account.getBalance());
        }

        // Subtract the amount from the balance
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create a transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("WITHDRAWAL");
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(request.getDescription() != null
                ? request.getDescription() : "Withdrawal");
        transaction.setTimestamp(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    /**
     * Transfer money between two accounts.
     *
     * Creates TWO transaction records:
     * - TRANSFER_OUT on the sender's account
     * - TRANSFER_IN on the receiver's account
     *
     * @Transactional ensures BOTH records are saved or NEITHER is.
     */
    @Transactional
    public TransactionResponse transfer(Long fromAccountId, TransferRequest request, User user) {
        // Get the sender's account (verifies ownership)
        Account fromAccount = accountService.getAccountEntity(fromAccountId, user);

        // Get the target account
        Account toAccount = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Target account not found: " + request.getTargetAccountNumber()));

        // Prevent transferring to your own account
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            throw new BankException("Cannot transfer money to the same account.");
        }

        // Check for sufficient funds
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BankException(
                    "Insufficient funds. Available balance: ₹" + fromAccount.getBalance());
        }

        // Deduct from sender
        BigDecimal senderNewBalance = fromAccount.getBalance().subtract(request.getAmount());
        fromAccount.setBalance(senderNewBalance);
        accountRepository.save(fromAccount);

        // Add to receiver
        BigDecimal receiverNewBalance = toAccount.getBalance().add(request.getAmount());
        toAccount.setBalance(receiverNewBalance);
        accountRepository.save(toAccount);

        String desc = request.getDescription() != null ? request.getDescription() : "Transfer";

        // Record TRANSFER_OUT for sender
        Transaction outTxn = new Transaction();
        outTxn.setAccount(fromAccount);
        outTxn.setType("TRANSFER_OUT");
        outTxn.setAmount(request.getAmount());
        outTxn.setBalanceAfter(senderNewBalance);
        outTxn.setDescription(desc + " → " + toAccount.getAccountNumber());
        outTxn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(outTxn);

        // Record TRANSFER_IN for receiver
        Transaction inTxn = new Transaction();
        inTxn.setAccount(toAccount);
        inTxn.setType("TRANSFER_IN");
        inTxn.setAmount(request.getAmount());
        inTxn.setBalanceAfter(receiverNewBalance);
        inTxn.setDescription(desc + " ← " + fromAccount.getAccountNumber());
        inTxn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(inTxn);

        return mapToResponse(outTxn);
    }

    /**
     * Get transaction history for a specific account.
     */
    public List<TransactionResponse> getTransactionHistory(Long accountId, User user) {
        // Verify account ownership
        Account account = accountService.getAccountEntity(accountId, user);

        List<Transaction> transactions =
                transactionRepository.findByAccountOrderByTimestampDesc(account);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all transactions (admin only).
     */
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Transaction entity to TransactionResponse DTO.
     */
    private TransactionResponse mapToResponse(Transaction txn) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(txn.getTransactionId());
        response.setType(txn.getType());
        response.setAmount(txn.getAmount());
        response.setBalanceAfter(txn.getBalanceAfter());
        response.setDescription(txn.getDescription());
        response.setTimestamp(txn.getTimestamp());
        response.setAccountNumber(txn.getAccount().getAccountNumber());
        return response;
    }
}
