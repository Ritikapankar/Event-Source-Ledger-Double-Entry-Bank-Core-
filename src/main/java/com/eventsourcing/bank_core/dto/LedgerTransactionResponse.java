
package com.eventsourcing.bank_core.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class LedgerTransactionResponse {

    private UUID id;
    private String description;
    private LocalDateTime createdAt;
}
