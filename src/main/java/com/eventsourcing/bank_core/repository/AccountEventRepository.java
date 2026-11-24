package com.eventsourcing.bank_core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventsourcing.bank_core.entity.AccountEvent;

public interface AccountEventRepository extends JpaRepository<AccountEvent, UUID> {
    List<AccountEvent> findByAccount_IdOrderByEventVersionAsc(UUID accountId);
    
    Optional<AccountEvent> findTopByAccount_IdOrderByEventVersionDesc(UUID accountId);
}
