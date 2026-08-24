package com.springbootbanking.controller;

import com.springbootbanking.dto.TransactionHistoryResponse;
import com.springbootbanking.service.TransactionHistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/history")
public class TransactionHistoryController {

    private final TransactionHistoryService historyService;

    public TransactionHistoryController(TransactionHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public TransactionHistoryResponse getHistory(@RequestHeader("accountNumber") Integer accountNumber) {
        return historyService.getHistory(accountNumber);
    }
}
