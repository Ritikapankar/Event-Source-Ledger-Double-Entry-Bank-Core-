package com.eventsourcing.bank_core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountReconciliationResponse {

    private UUID accountId;
    private BigDecimal actualBalance;
    private BigDecimal replayBalance;
    private boolean reconciled;
}