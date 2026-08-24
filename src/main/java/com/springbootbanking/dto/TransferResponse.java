package com.springbootbanking.dto;

import java.math.BigDecimal;

public class TransferResponse {

    private final boolean success;
    private final String message;
    private final BigDecimal balance; // BigDecimal preserves precision - never use double for money

    public TransferResponse(boolean success, String message, BigDecimal balance) {
        this.success = success;
        this.message = message;
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}