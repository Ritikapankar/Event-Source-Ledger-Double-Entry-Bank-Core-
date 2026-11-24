package com.eventsourcing.bank_core.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.eventsourcing.bank_core.dto.AccountEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "account_events",
    uniqueConstraints = {
    @UniqueConstraint (
        columnNames = {"account_id" , "event_version"}  
    )
    }
)
@Getter 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEvent {
 
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     private UUID id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "account_id", nullable = false)
     private Account account;

     @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     private AccountEventType eventType;

     @Column (nullable = false, precision = 19, scale = 2)
        private BigDecimal amount;

        @Column(name = "event_version", nullable = false)
        private Long eventVersion;

        private String description;

        @Column (nullable = false)
        private LocalDateTime createdAt;

        public AccountEvent(Account account, AccountEventType eventType, BigDecimal amount, Long eventVersion, String description) {
            this.account = account;
            this.eventType = eventType;
            this.amount = amount;
            this.eventVersion = eventVersion;
            this.description = description;
            this.createdAt = LocalDateTime.now();
        }
}
