package com.springbootbanking.domain.service;

import versions.v2.contract.request.TransferConfirmationRequest;
import versions.v2.contract.request.TransferLookupRequest;
import versions.v2.contract.response.TransferResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.model.Transaction;
import versions.v2.domain.model.TransactionType;
import versions.v2.domain.model.User;
import versions.v2.domain.validator.TransferValidator;
import versions.v2.repository.UserRepository;

public class TransferService {

    private final UserRepository repository;

    private final TransferValidator validator;

    public TransferService(
            UserRepository repository,
            TransferValidator validator) {

        this.repository = repository;
        this.validator = validator;
    }

    public TransferResponse lookupReceiver(
            TransferLookupRequest request) {

        return validator.validateLookup(
                request,
                repository);
    }

    public TransferResponse transfer(
            TransferConfirmationRequest request) {

        TransferResponse validationResult =
                validator.validate(
                        request,
                        repository);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        User sender = request.getUser();

        BankAccount senderAccount =
                sender.getBankAccount();

        User receiverUser = repository.findByAccountNumber(
                Integer.parseInt(
                        request.getReceiverAccountNumber()));

        BankAccount receiver =
                receiverUser.getBankAccount();

        double amount = request.getAmount();

        senderAccount.withdraw(amount);

        Transaction senderTransaction = new Transaction(
                TransactionType.TRANSFER,
                amount,
                "Transferred to Account "
                        + receiver.getAccountNumber(),
                senderAccount.getBalance());

        senderAccount.addTransaction(senderTransaction);

        receiver.deposit(amount);

        Transaction receiverTransaction = new Transaction(
                TransactionType.RECEIVED,
                amount,
                "Received from Account "
                        + senderAccount.getAccountNumber(),
                receiver.getBalance());

        receiver.addTransaction(receiverTransaction);

        return new TransferResponse(
                true,
                "₹" + amount
                        + " transferred successfully to "
                        + receiver.getAccountHolderName(),
                senderAccount.getBalance());

    }

}
