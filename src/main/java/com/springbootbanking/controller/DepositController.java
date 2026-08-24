package com.springbootbanking.controller;

import com.springbootbanking.dto.DepositRequest;
import com.springbootbanking.dto.DepositResponse;
import com.springbootbanking.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/deposit")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService){
        this.depositService  = depositService;
    }

    @PostMapping
    public ResponseEntity<DepositResponse> deposit(@Valid @RequestBody DepositRequest request){
        DepositResponse depositResponse = depositService.deposit(request);
        return ResponseEntity.ok(depositResponse); // 200 OK — deposit is an action, not a resource creation
    }
}

