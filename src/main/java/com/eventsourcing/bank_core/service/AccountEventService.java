package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.dto.AccountEventType;
import com.eventsourcing.bank_core.dto.AccountReconciliationResponse;
import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountEvent;
import com.eventsourcing.bank_core.repository.AccountEventRepository;
import com.eventsourcing.bank_core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountEventService {

    private final AccountEventRepository accountEventRepository;
    private final AccountRepository accountRepository;

    // Replay balance from events
    @Transactional(readOnly = true)
    public BigDecimal replayBalance(UUID accountId) {

        List<AccountEvent> events = accountEventRepository
                .findByAccount_IdOrderByEventVersionAsc(accountId);

        BigDecimal balance = BigDecimal.ZERO;

        for (AccountEvent event : events) {

            if (event.getEventType() == AccountEventType.MONEY_DEPOSITED) {
                balance = balance.add(event.getAmount());
            }
            else if (event.getEventType() == AccountEventType.MONEY_WITHDRAWN) {
                balance = balance.subtract(event.getAmount());
            }
        }

        return balance;
    }

    // Record account event
    @Transactional
    public void recordEvent(Account account, AccountEventType eventType,
                           BigDecimal amount, String description) {

        Long latestVersion = accountEventRepository
                .findTopByAccount_IdOrderByEventVersionDesc(account.getId())
                .map(AccountEvent::getEventVersion)
                .orElse(0L);

        Long nextVersion = latestVersion + 1;

        AccountEvent event = new AccountEvent(
                account,
                eventType,
                amount,
                nextVersion,
                description
        );

        accountEventRepository.save(event);
    }

    // Balance reconciliation
    @Transactional(readOnly = true)
    public AccountReconciliationResponse reconcileBalance(UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        BigDecimal actualBalance = account.getBalance();
        BigDecimal replayBalance = replayBalance(accountId);

        boolean reconciled = actualBalance.compareTo(replayBalance) == 0;

        return new AccountReconciliationResponse(
                accountId,
                actualBalance,
                replayBalance,
                reconciled
        );
    }
}