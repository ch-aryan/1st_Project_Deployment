// src/main/java/com/springbootbanking/service/DepositValidator.java
package com.springbootbanking.service;

import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.exception.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositValidator {

    // Validates using the real BankAccount fetched from the database by the service
    public void validate(BankAccount account, String pin, BigDecimal amount) {

        // 1. Account active check (and handle orphaned accounts)
        if (account.getCustomer() == null) {
            throw new AccountNotFoundException("Account exists but has no associated customer record");
        }
        if (!account.getCustomer().isActive()) {
            throw new AccountInactiveException("Account is inactive or locked");
        }

        // 2. PIN verification (against the pin stored in DB, not from the request)
        if (!account.verifyPin(pin.trim())) {
            throw new InvalidPinException("Incorrect PIN");
        }

        // 3. Amount > 0 (already validated by @DecimalMin on DTO, but defensive)
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
    }
}