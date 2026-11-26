package com.eventsourcing.bank_core.controller;


import com.eventsourcing.bank_core.dto.AccountResponse;
import com.eventsourcing.bank_core.dto.CreateAccountRequest;
import com.eventsourcing.bank_core.dto.MoneyRequest;
import com.eventsourcing.bank_core.service.AccountEventService;
import com.eventsourcing.bank_core.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountEventService accountEventService;

    // Create Account
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get All Accounts
    @GetMapping
    public ResponseEntity<Object> getAllAccounts() {

        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // Get Account By ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccountById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(accountService.getAccountById(id));
    }

     @PostMapping ("/{id}/deposit")
    public ResponseEntity<Object> depositMoney(
            @PathVariable UUID id,
            @Valid @RequestBody MoneyRequest request) {

           AccountResponse response = accountService.depositMoney(id, request.getAmount());
                    return ResponseEntity.ok(response);
            }

             @PostMapping ("/{id}/withdraw")
    public ResponseEntity<Object> withdrawMoney(
            @PathVariable UUID id,
            @Valid @RequestBody MoneyRequest request) {

           AccountResponse response = accountService.withdrawMoney(id, request.getAmount());
                    return ResponseEntity.ok(response);
            }

            @GetMapping("/{id}/replay-balance")
public ResponseEntity<BigDecimal> replayBalance(@PathVariable UUID id) {

    BigDecimal balance = accountEventService.replayBalance(id);

    return ResponseEntity.ok(balance);
}
        }


