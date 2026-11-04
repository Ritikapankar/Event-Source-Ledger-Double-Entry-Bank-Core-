package com.eventsourcing.bank_core.dto;



import com.eventsourcing.bank_core.entity.AccountStatus;
import com.eventsourcing.bank_core.entity.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AccountResponse {

    private UUID id;
    private String accountNumber;
    private String accountHolderName;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
}

