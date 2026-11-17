
package com.eventsourcing.bank_core.repository;

import com.eventsourcing.bank_core.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerTransactionRepository
        extends JpaRepository<LedgerTransaction, UUID> {
}
