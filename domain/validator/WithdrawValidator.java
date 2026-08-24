package com.springbootbanking.domain.validator;

import versions.v2.contract.request.WithdrawRequest;
import versions.v2.contract.response.WithdrawlResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.validator.rules.AmountRules;
import versions.v2.domain.validator.rules.BalanceRules;
import versions.v2.domain.validator.rules.PinRules;

public class WithdrawValidator {

    public WithdrawlResponse validate(WithdrawRequest request) {

        BankAccount account =
                request.getUser().getBankAccount();

        double balance = account.getBalance();

        if (!AmountRules.isValid(request.getAmount())) {
            return new WithdrawlResponse(
                    false,
                    "Amount should be greater than 0.",
                    balance);
        }

        if (!PinRules.isValid(request.getPin())) {
            return new WithdrawlResponse(
                    false,
                    "PIN must contain 4 or 6 digits.",
                    balance);
        }

        if (!account.verifyPin(request.getPin())) {
            return new WithdrawlResponse(
                    false,
                    "Incorrect PIN.",
                    balance);
        }

        if (!BalanceRules.hasSufficientBalance(
                balance,
                request.getAmount())) {

            return new WithdrawlResponse(
                    false,
                    "Insufficient Balance.",
                    balance);
        }

        return new WithdrawlResponse(
                true,
                "Validation successful.",
                balance);

    }

}
