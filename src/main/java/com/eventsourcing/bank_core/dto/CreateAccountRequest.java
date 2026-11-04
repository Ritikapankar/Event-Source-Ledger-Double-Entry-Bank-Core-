package com.eventsourcing.bank_core.dto;

import com.eventsourcing.bank_core.entity.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Account holder name is required")
    @Size(max = 100)
    private String accountHolderName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
