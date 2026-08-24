package com.springbootbanking.service;

import com.springbootbanking.dto.TransferRequest;
import com.springbootbanking.dto.TransferResponse;
import com.springbootbanking.entity.BankAccount;
import com.springbootbanking.entity.Transaction;
import com.springbootbanking.entity.TransactionType;
import com.springbootbanking.exception.AccountNotFoundException;
import com.springbootbanking.exception.InsufficientBalanceException;
import com.springbootbanking.exception.InvalidPinException;
import com.springbootbanking.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final BankAccountRepository accountRepository;

    public TransferService(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransferResponse transfer(Integer senderAccountNumber, TransferRequest request) {
        // Use AccountNotFoundException instead of RuntimeException
        BankAccount sender = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Sender account not found: " + senderAccountNumber));

        BankAccount receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Receiver account not found: " + request.getReceiverAccountNumber()));

        if (!sender.verifyPin(request.getPin())) {
            throw new InvalidPinException("Incorrect PIN");
        }

        BigDecimal amount = request.getAmount();
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance", sender.getBalance(), amount);
        }

        sender.withdraw(amount);
        receiver.deposit(amount);

        sender.addTransaction(new Transaction(TransactionType.TRANSFER, amount, sender.getBalance(),
                "Transferred to " + receiver.getAccountNumber(), sender));
        receiver.addTransaction(new Transaction(TransactionType.RECEIVED, amount, receiver.getBalance(),
                "Received from " + sender.getAccountNumber(), receiver));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Return BigDecimal directly - never convert money to double
        return new TransferResponse(true, "Transfer successful", sender.getBalance());
    }
}