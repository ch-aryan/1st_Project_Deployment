package com.springbootbanking.domain.validator;

import versions.v2.contract.request.DepositRequest;
import versions.v2.contract.response.DepositResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.validator.rules.AmountRules;
import versions.v2.domain.validator.rules.BalanceRules;
import versions.v2.domain.validator.rules.PinRules;

public class DepositValidator {

    public DepositResponse validate(DepositRequest request) {

        BankAccount account =
                request.getUser().getBankAccount();

        double balance = account.getBalance();

        if (!AmountRules.isValid(request.getAmount())) {
            return new DepositResponse(
                    false,
                    "Amount should be greater than 0.",
                    balance);
        }

        if (!PinRules.isValid(request.getPin())) {
            return new DepositResponse(
                    false,
                    "PIN must contain 4 or 6 digits.",
                    balance);
        }

        if (!account.verifyPin(request.getPin())) {
            return new DepositResponse(
                    false,
                    "Incorrect PIN.",
                    balance);
        }

        double newBalance =
                balance + request.getAmount();

        if (!BalanceRules.withinMaximumLimit(
                newBalance)) {

            return new DepositResponse(
                    false,
                    "Maximum account balance limit (10,00,000) exceeded.",
                    balance);
        }

        return new DepositResponse(
                true,
                "Validation successful.",
                balance);

    }

}
