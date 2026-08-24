package com.springbootbanking.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransferRequest {

    @NotNull(message = "Receiver account number is required")
    private final Integer receiverAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have max 2 decimal places")
    private final BigDecimal amount;

    @NotBlank(message = "PIN is required")
    @Pattern(regexp = "^\\d{4}(\\d{2})?$", message = "PIN must be exactly 4 or 6 digits")
    private final String pin;

    public TransferRequest(Integer receiverAccountNumber, BigDecimal amount, String pin) {
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.pin = pin;
    }

    public Integer getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPin() {
        return pin;
    }
}
