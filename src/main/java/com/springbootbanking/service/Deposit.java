package com.springbootbanking.service;

import com.springbootbanking.dto.DepositRequest;
import com.springbootbanking.dto.DepositResponse;

public interface Deposit {
    public DepositResponse deposit(DepositRequest request);
}
