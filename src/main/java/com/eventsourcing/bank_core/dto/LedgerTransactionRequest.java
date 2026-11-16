
package com.eventsourcing.bank_core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LedgerTransactionRequest {

    @NotBlank
    private String description;

    @NotBlank
    private String debitAccountCode;

    @NotBlank
    private String creditAccountCode;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal debitAmount;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal creditAmount;
}
