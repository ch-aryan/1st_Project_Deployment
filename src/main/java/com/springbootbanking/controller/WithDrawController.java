package com.springbootbanking.controller;

import com.springbootbanking.dto.WithdrawRequest;
import com.springbootbanking.dto.WithdrawlResponse;
import com.springbootbanking.service.WithdrawService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/withdraw")
public class WithDrawController {

    private final WithdrawService withdrawService;

    protected WithDrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }
    @PostMapping
    public ResponseEntity<WithdrawlResponse> withdrawController(@Valid @RequestBody WithdrawRequest request){
        WithdrawlResponse withdrawResponse = withdrawService.withdraw(request);
        return ResponseEntity.status(HttpStatus.OK).body(withdrawResponse);
    }
}
