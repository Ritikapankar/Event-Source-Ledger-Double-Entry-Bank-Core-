
package com.eventsourcing.bank_core.config;

import com.eventsourcing.bank_core.entity.LedgerAccount;
import com.eventsourcing.bank_core.entity.LedgerAccountType;
import com.eventsourcing.bank_core.repository.LedgerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerAccountInitializer implements CommandLineRunner {

    private final LedgerAccountRepository ledgerAccountRepository;

    @Override
    public void run(String... args) {

        if (ledgerAccountRepository.findByAccountCode("CASH_ASSET").isEmpty()) {
            ledgerAccountRepository.save(
                    new LedgerAccount(
                            "CASH_ASSET",
                            "Bank Cash Asset",
                            LedgerAccountType.ASSET
                    )
            );
        }

        if (ledgerAccountRepository.findByAccountCode("CUSTOMER_DEPOSITS").isEmpty()) {
            ledgerAccountRepository.save(
                    new LedgerAccount(
                            "CUSTOMER_DEPOSITS",
                            "Customer Deposits Liability",
                            LedgerAccountType.LIABILITY
                    )
            );
        }
    }
}

