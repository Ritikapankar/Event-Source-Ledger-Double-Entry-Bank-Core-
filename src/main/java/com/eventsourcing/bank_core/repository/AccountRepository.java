package com.eventsourcing.bank_core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventsourcing.bank_core.entity.Account;

public interface AccountRepository extends JpaRepository<Account,UUID> {
     
    Optional<Account> findByAccountNumber( String accountNumber);

    abstract boolean existsByAccountNumber(String accountNumber);
}
