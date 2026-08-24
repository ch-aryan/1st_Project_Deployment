// src/main/java/com/springbootbanking/service/WithdrawService.java
package com.springbootbanking.service;

import com.springbootbanking.dto.WithdrawRequest;
import com.springbootbanking.dto.WithdrawlResponse;
import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.entity.Transaction;
import com.springbootbanking.entity.TransactionType;
import com.springbootbanking.exception.*;
import com.springbootbanking.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WithdrawService implements Withdraw {

    private final WithdrawValidator validator;
    private final BankAccountRepository accountRepository;

    public WithdrawService(WithdrawValidator validator, BankAccountRepository accountRepository) {
        this.validator = validator;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public WithdrawlResponse withdraw(WithdrawRequest request) {
        // 1. Fetch the REAL account from the database using the accountNumber the client sent
        BankAccount account = accountRepository
                .findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + request.getAccountNumber()));

        // 2. Validate against the real DB account (PIN check, active check, balance check)
        validator.validate(account, request.getPin(), request.getAmount());

        BigDecimal amount = request.getAmount();

        // 3. Perform withdrawal on the real account
        account.withdraw(amount);

        // 4. Persist transaction record
        Transaction transaction = new Transaction(
                TransactionType.WITHDRAW,
                amount,
                account.getBalance(),
                "Cash Withdrawal",
                account
        );
        account.addTransaction(transaction);

        return new WithdrawlResponse(
                true,
                "?" + amount + " withdrawn successfully.",
                account.getBalance()
        );
    }
}
