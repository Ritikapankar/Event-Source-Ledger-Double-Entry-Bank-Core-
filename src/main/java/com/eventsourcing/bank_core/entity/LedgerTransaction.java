package com.eventsourcing.bank_core.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ledger_transaction")
@Getter
@NoArgsConstructor
public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "transaction",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    private List<LedgerEntry> entries = new ArrayList<>();

    public LedgerTransaction(String description) {
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void addEntry(LedgerEntry entry) {
        entries.add(entry);
        entry.setTransaction(this);
    }
}

