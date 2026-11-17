package com.eventsourcing.bank_core.repository;


import com.eventsourcing.bank_core.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LedgerAccountRepository
        extends JpaRepository<LedgerAccount, UUID> {

    Optional<LedgerAccount> findByAccountCode(String accountCode);
}

