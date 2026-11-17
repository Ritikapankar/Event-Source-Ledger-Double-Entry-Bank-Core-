
package com.eventsourcing.bank_core.controller;

import com.eventsourcing.bank_core.dto.LedgerTransactionRequest;
import com.eventsourcing.bank_core.dto.LedgerTransactionResponse;
import com.eventsourcing.bank_core.entity.LedgerTransaction;
import com.eventsourcing.bank_core.service.LedgerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    // Create a new double-entry ledger transaction
    @PostMapping("/transactions")
    public ResponseEntity<LedgerTransactionResponse> createTransaction(
            @Valid @RequestBody LedgerTransactionRequest request) {

        // Call LedgerService
        LedgerTransaction transaction =
                ledgerService.recordTransaction(
                        request.getDescription(),
                        request.getDebitAccountCode(),
                        request.getCreditAccountCode(),
                        request.getDebitAmount(),
                        request.getCreditAmount()
                );

        // Convert Entity into Response DTO
        LedgerTransactionResponse response =
                LedgerTransactionResponse.builder()
                        .id(transaction.getId())
                        .description(transaction.getDescription())
                        .createdAt(transaction.getCreatedAt())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
