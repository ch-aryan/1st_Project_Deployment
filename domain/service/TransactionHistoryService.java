package com.springbootbanking.domain.service;

import versions.v2.contract.request.TransactionHistoryRequest;
import versions.v2.contract.response.TransactionHistoryResponse;
import versions.v2.domain.model.Transaction;

import java.util.List;

public class TransactionHistoryService {

    public TransactionHistoryResponse getHistory(
            TransactionHistoryRequest request) {

        List<Transaction> transactions =
                request.getUser()
                        .getBankAccount()
                        .getTransactionHistory();

        return new TransactionHistoryResponse(
                true,
                "History retrieved.",
                transactions);

    }

}
