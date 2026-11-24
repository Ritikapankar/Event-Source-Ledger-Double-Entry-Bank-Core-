package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountStatus;
import com.eventsourcing.bank_core.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountEventService accountEventService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void depositRejectsInvalidAmountBeforeCheckingAccountStatus() {
        UUID accountId = UUID.randomUUID();
        Account account = Account.builder()
                .status(AccountStatus.CLOSED)
                .balance(BigDecimal.ZERO)
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.depositMoney(accountId, null));
    }

    @Test
    void depositHandlesNullExistingBalance() {
        UUID accountId = UUID.randomUUID();
        Account account = Account.builder()
                .status(AccountStatus.ACTIVE)
                .balance(null)
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenAnswer(invocation -> invocation.getArgument(0));

        accountService.depositMoney(accountId, new BigDecimal("25.50"));

        assertEquals(new BigDecimal("25.50"), account.getBalance());
    }
}
