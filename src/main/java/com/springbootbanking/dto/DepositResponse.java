package com.springbootbanking.dto;

import java.math.BigDecimal;

public class DepositResponse {
    private boolean status;
    private String message;
    private BigDecimal balance;

    public DepositResponse(boolean status, String message, BigDecimal balance) {
        this.status = status;
        this.message = message;
        this.balance = balance;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
