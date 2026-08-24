// src/main/java/com/springbootbanking/exception/InsufficientBalanceException.java
package com.springbootbanking.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    private final BigDecimal availableBalance;
    private final BigDecimal requestedAmount;

    public InsufficientBalanceException(String message, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(message);
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public BigDecimal getAvailableBalance() { return availableBalance; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
}