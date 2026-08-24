package com.springbootbanking.domain.service;

import versions.v2.contract.request.BalanceRequest;
import versions.v2.contract.response.BalanceResponse;

public class BalanceService {

    public BalanceResponse checkBalance(
            BalanceRequest request) {

        double balance =
                request.getUser()
                        .getBankAccount()
                        .getBalance();

        return new BalanceResponse(
                true,
                "Balance retrieved.",
                balance);

    }

}
