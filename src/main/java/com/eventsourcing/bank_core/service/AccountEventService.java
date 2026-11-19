
package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.dto.AccountEventType;
import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountEvent;
import com.eventsourcing.bank_core.repository.AccountEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountEventService {

    private final AccountEventRepository accountEventRepository;

    @Transactional
    public AccountEvent recordEvent(
            Account account,
            AccountEventType eventType,
            BigDecimal amount,
            String description) {

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

        return accountEventRepository.save(event);
    }
}
