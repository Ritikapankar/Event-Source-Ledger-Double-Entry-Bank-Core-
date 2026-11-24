
package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.dto.AccountEventType;
import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountEvent;
import com.eventsourcing.bank_core.repository.AccountEventRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountEventService {

    private final AccountEventRepository accountEventRepository;

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
    }

