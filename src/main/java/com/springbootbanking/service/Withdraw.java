package com.springbootbanking.service;

import com.springbootbanking.dto.WithdrawRequest;
import com.springbootbanking.dto.WithdrawlResponse;

public interface Withdraw {
    WithdrawlResponse withdraw(WithdrawRequest request);
}
