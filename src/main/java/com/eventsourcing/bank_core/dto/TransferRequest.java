package com.eventsourcing.bank_core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransferRequest {

    @NotNull
    private UUID senderAccountId;

    @NotNull
    private UUID receiverAccountId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}