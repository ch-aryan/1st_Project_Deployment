// src/main/java/com/springbootbanking/service/DepositService.java
package com.springbootbanking.service;

import com.springbootbanking.dto.DepositRequest;
import com.springbootbanking.dto.DepositResponse;
import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.entity.Transaction;
import com.springbootbanking.entity.TransactionType;
import com.springbootbanking.exception.*;
import com.springbootbanking.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DepositService implements Deposit {

    private final DepositValidator validator;
    private final BankAccountRepository accountRepository;

    public DepositService(DepositValidator validator, BankAccountRepository accountRepository) {
        this.validator = validator;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public DepositResponse deposit(DepositRequest request) {
        // 1. Fetch the REAL account from the database using the accountNumber the client sent
        BankAccount account = accountRepository
                .findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + request.getAccountNumber()));

        // 2. Validate against the real DB account (PIN check, active check, amount check)
        validator.validate(account, request.getPin(), request.getAmount());

        BigDecimal amount = request.getAmount();

        // 3. Perform deposit on the real account
        account.deposit(amount);

        // 4. Persist transaction record
        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                account.getBalance(),
                "Cash Deposit",
                account
        );
        account.addTransaction(transaction);

        return new DepositResponse(
                true,
                "₹" + amount + " deposited successfully.",
                account.getBalance()
        );
    }
}
