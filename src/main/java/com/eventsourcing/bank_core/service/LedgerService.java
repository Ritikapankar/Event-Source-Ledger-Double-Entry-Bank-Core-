
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
            BigDecimal creditAmount
    ) {

        // 1. Validate amounts
        if (debitAmount == null || creditAmount == null
                || debitAmount.compareTo(BigDecimal.ZERO) <= 0
                || creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amounts must be greater than zero");
        }

        // 2. Validate double-entry rule
        if (debitAmount.compareTo(creditAmount) != 0) {
            throw new IllegalArgumentException(
                    "Debit and Credit amounts must be equal"
            );
        }

        // 3. Fetch ledger accounts
        LedgerAccount debitAccount = ledgerAccountRepository
                .findByAccountCode(debitAccountCode)
                .orElseThrow(() -> new RuntimeException("Debit account not found"));

        LedgerAccount creditAccount = ledgerAccountRepository
                .findByAccountCode(creditAccountCode)
                .orElseThrow(() -> new RuntimeException("Credit account not found"));

        // 4. Create transaction
        LedgerTransaction transaction =
                new LedgerTransaction(description);

        // 5. Create debit entry
        LedgerEntry debitEntry = new LedgerEntry(
                debitAccount,
                LedgerEntryType.DEBIT,
                debitAmount
        );

        // 6. Create credit entry
        LedgerEntry creditEntry = new LedgerEntry(
                creditAccount,
                LedgerEntryType.CREDIT,
                creditAmount
        );

        // 7. Add entries to transaction
        transaction.addEntry(debitEntry);
        transaction.addEntry(creditEntry);

        // 8. Save transaction and entries
        return ledgerTransactionRepository.save(transaction);
    }
}
