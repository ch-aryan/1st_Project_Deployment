package com.springbootbanking.service;

import com.springbootbanking.dto.TransactionHistoryResponse;
import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionHistoryService {

    private final BankAccountRepository accountRepository;

    public TransactionHistoryService(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public TransactionHistoryResponse getHistory(Integer accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new TransactionHistoryResponse(true, "History retrieved.", account.getTransactions());
    }
}
