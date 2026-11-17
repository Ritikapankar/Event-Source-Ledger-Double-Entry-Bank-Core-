
package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.entity.*;
import com.eventsourcing.bank_core.repository.LedgerAccountRepository;
import com.eventsourcing.bank_core.repository.LedgerTransactionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerAccountRepository ledgerAccountRepository;
    private final LedgerTransactionRepository ledgerTransactionRepository;

    @Transactional
    public LedgerTransaction recordTransaction(
            String description,
            String debitAccountCode,
            String creditAccountCode,
            BigDecimal debitAmount,
            BigDecimal creditAmount) {

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (debitAmount == null || debitAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be greater than zero");
        }
        if (creditAmount == null || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be greater than zero");
        }

        LedgerAccount debitAccount = ledgerAccountRepository.findByAccountCode(debitAccountCode)
                .orElseThrow(() -> new RuntimeException("Debit ledger account not found: " + debitAccountCode));
        LedgerAccount creditAccount = ledgerAccountRepository.findByAccountCode(creditAccountCode)
                .orElseThrow(() -> new RuntimeException("Credit ledger account not found: " + creditAccountCode));

        LedgerTransaction transaction = new LedgerTransaction(description);

        LedgerEntry debitEntry = new LedgerEntry(
                debitAccount,
                null,
                LedgerEntryType.DEBIT,
                debitAmount
        );

        LedgerEntry creditEntry = new LedgerEntry(
                creditAccount,
                null,
                LedgerEntryType.CREDIT,
                creditAmount
        );

        transaction.addEntry(debitEntry);
        transaction.addEntry(creditEntry);

        return ledgerTransactionRepository.save(transaction);
    }

    @Transactional
    public LedgerTransaction recordTransfer(
            Account sender,
            Account receiver,
            BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }

        LedgerAccount customerDeposits = ledgerAccountRepository
                .findByAccountCode("CUSTOMER_DEPOSITS")
                .orElseThrow(() ->
                        new RuntimeException("Customer deposits ledger account not found"));

        LedgerTransaction transaction = new LedgerTransaction(
                "Transfer from " + sender.getAccountNumber()
                        + " to " + receiver.getAccountNumber()
        );

        LedgerEntry debitEntry = new LedgerEntry(
                customerDeposits,
                sender,
                LedgerEntryType.DEBIT,
                amount
        );

        LedgerEntry creditEntry = new LedgerEntry(
                customerDeposits,
                receiver,
                LedgerEntryType.CREDIT,
                amount
        );

        transaction.addEntry(debitEntry);
        transaction.addEntry(creditEntry);

        return ledgerTransactionRepository.save(transaction);
    }

}
