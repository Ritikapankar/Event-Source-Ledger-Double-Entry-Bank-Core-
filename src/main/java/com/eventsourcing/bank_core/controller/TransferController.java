
package com.eventsourcing.bank_core.controller;

import com.eventsourcing.bank_core.dto.TransferRequest;
import com.eventsourcing.bank_core.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transferMoney(
            @Valid @RequestBody TransferRequest request) {

        transferService.transferMoney(request);

        return ResponseEntity.ok("Money transferred successfully");
    }
}
