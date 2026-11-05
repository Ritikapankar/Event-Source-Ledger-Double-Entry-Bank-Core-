package com.eventsourcing.bank_core.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter 
public class MoneyRequest {
    
    @NotNull (message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be a positive number")
    private BigDecimal amount;
}
