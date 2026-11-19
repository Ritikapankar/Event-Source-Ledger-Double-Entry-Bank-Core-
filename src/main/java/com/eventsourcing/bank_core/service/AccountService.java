
package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.dto.AccountResponse;
import com.eventsourcing.bank_core.dto.CreateAccountRequest;
import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountStatus;
import com.eventsourcing.bank_core.dto.AccountEventType;
import com.eventsourcing.bank_core.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountEventService accountEventService;

    // Create Account
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountHolderName(request.getAccountHolderName())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);

        accountEventService.recordEvent(
                savedAccount,
                AccountEventType.ACCOUNT_CREATED,
                BigDecimal.ZERO,
                "Account created"
        );

        return mapToResponse(savedAccount);
    }

    // Get All Accounts
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Account By ID
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(UUID id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found: " + id));

        return mapToResponse(account);
    }

    // Deposit Money
    @Transactional
    public AccountResponse depositMoney(UUID accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(amount));

        Account updatedAccount = accountRepository.save(account);

        accountEventService.recordEvent(
                updatedAccount,
                AccountEventType.MONEY_DEPOSITED,
                amount,
                "Money deposited into account"
        );

        return mapToResponse(updatedAccount);
    }

    // Withdraw Money
    @Transactional
    public AccountResponse withdrawMoney(UUID accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found: " + accountId));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Account updatedAccount = accountRepository.save(account);

        accountEventService.recordEvent(
                updatedAccount,
                AccountEventType.MONEY_WITHDRAWN,
                amount,
                "Money withdrawn from account"
        );

        return mapToResponse(updatedAccount);
    }

    // Generate Account Number
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    // Entity to DTO Mapping
    private AccountResponse mapToResponse(Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
