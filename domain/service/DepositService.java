package com.springbootbanking.domain.service;

import versions.v2.contract.request.DepositRequest;
import versions.v2.contract.response.DepositResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.model.Transaction;
import versions.v2.domain.model.TransactionType;
import versions.v2.domain.validator.DepositValidator;

public class DepositService {

    private final DepositValidator validator;

    public DepositService(DepositValidator validator) {

        this.validator = validator;
    }

    public DepositResponse deposit(DepositRequest request) {

        DepositResponse validationResult =
                validator.validate(request);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        BankAccount account =
                request.getUser().getBankAccount();

        double amount = request.getAmount();

        account.deposit(amount);

        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                "Cash Deposit",
                account.getBalance());

        account.addTransaction(transaction);

        return new DepositResponse(
                true,
                "₹" + amount + " deposited successfully.",
                account.getBalance());

    }

}
