package com.springbootbanking.controller;

import com.springbootbanking.dto.TransferRequest;
import com.springbootbanking.dto.TransferResponse;
import com.springbootbanking.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @RequestHeader("accountNumber") Integer accountNumber,
            @Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.transfer(accountNumber, request);
        return ResponseEntity.ok(response); // 200 OK - transfer is an action, not a resource creation
    }
}