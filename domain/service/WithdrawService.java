package com.springbootbanking.domain.service;

import versions.v2.contract.request.WithdrawRequest;
import versions.v2.contract.response.WithdrawlResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.model.Transaction;
import versions.v2.domain.model.TransactionType;
import versions.v2.domain.validator.WithdrawValidator;

public class WithdrawService {

    private final WithdrawValidator validator;

    public WithdrawService(WithdrawValidator validator) {

        this.validator = validator;
    }

    public WithdrawlResponse withdraw(WithdrawRequest request) {

        WithdrawlResponse validationResult =
                validator.validate(request);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        BankAccount account =
                request.getUser().getBankAccount();

        double amount = request.getAmount();

        account.withdraw(amount);

        Transaction transaction = new Transaction(
                TransactionType.WITHDRAW,
                amount,
                "Cash Withdrawal",
                account.getBalance());

        account.addTransaction(transaction);

        return new WithdrawlResponse(
                true,
                "₹" + amount + " withdrawn successfully.",
                account.getBalance());

    }

}
