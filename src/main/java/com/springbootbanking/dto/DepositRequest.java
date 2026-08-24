package com.springbootbanking.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DepositRequest {

    // Client sends their account number - server looks up the real account from DB
    @NotNull(message = "Account number is required")
    private final Integer accountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have max 2 decimal places")
    private final BigDecimal amount;

    @NotBlank(message = "PIN is required")
    @Pattern(regexp = "^\\d{4}(\\d{2})?$", message = "PIN must be exactly 4 or 6 digits")
    private final String pin;

    public DepositRequest(Integer accountNumber, BigDecimal amount, String pin) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.pin = pin;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPin() {
        return pin;
    }
}
