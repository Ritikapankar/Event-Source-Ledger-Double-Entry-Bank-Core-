
package com.eventsourcing.bank_core.service;

import com.eventsourcing.bank_core.dto.TransferRequest;
import com.eventsourcing.bank_core.entity.Account;
import com.eventsourcing.bank_core.entity.AccountStatus;
import com.eventsourcing.bank_core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;

     private final LedgerService ledgerService;
    
@Transactional
public void transferMoney(TransferRequest request) {

    UUID senderId = request.getSenderAccountId();
    UUID receiverId = request.getReceiverAccountId();
    BigDecimal amount = request.getAmount();

    if (senderId.equals(receiverId)) {
        throw new IllegalArgumentException(
                "Sender and receiver cannot be same");
    }

    Account sender = accountRepository.findById(senderId)
            .orElseThrow(() ->
                    new RuntimeException("Sender account not found"));

    Account receiver = accountRepository.findById(receiverId)
            .orElseThrow(() ->
                    new RuntimeException("Receiver account not found"));

    if (sender.getStatus() != AccountStatus.ACTIVE ||
            receiver.getStatus() != AccountStatus.ACTIVE) {
        throw new RuntimeException("Both accounts must be active");
    }

    if (sender.getBalance().compareTo(amount) < 0) {
        throw new RuntimeException("Insufficient balance");
    }

    sender.setBalance(sender.getBalance().subtract(amount));
    receiver.setBalance(receiver.getBalance().add(amount));

    accountRepository.save(sender);
    accountRepository.save(receiver);

    ledgerService.recordTransfer(sender, receiver, amount);
}

}
