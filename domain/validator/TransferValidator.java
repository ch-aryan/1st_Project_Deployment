package com.springbootbanking.domain.validator;

import versions.v2.contract.request.TransferConfirmationRequest;
import versions.v2.contract.request.TransferLookupRequest;
import versions.v2.contract.response.TransferResponse;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.model.User;
import versions.v2.domain.validator.rules.AmountRules;
import versions.v2.domain.validator.rules.BalanceRules;
import versions.v2.domain.validator.rules.PinRules;
import versions.v2.repository.UserRepository;

public class TransferValidator {

    public TransferResponse validateLookup(
            TransferLookupRequest request,
            UserRepository repository) {

        BankAccount sender =
                request.getUser().getBankAccount();

        double senderBalance = sender.getBalance();

        User receiverUser = repository.findByAccountNumber(
                Integer.parseInt(
                        request.getReceiverAccountNumber()));

        if (receiverUser == null) {

            return new TransferResponse(
                    false,
                    "Receiver account does not exist.",
                    senderBalance);

        }

        BankAccount receiver =
                receiverUser.getBankAccount();

        if (sender.getAccountNumber()
                .equals(receiver.getAccountNumber())) {

            return new TransferResponse(
                    false,
                    "Cannot transfer money to your own account.",
                    senderBalance);

        }

        return new TransferResponse(
                true,
                "Validation successful.",
                senderBalance,
                receiver.getAccountHolderName());

    }

    public TransferResponse validate(
            TransferConfirmationRequest request,
            UserRepository repository) {

        BankAccount sender =
                request.getUser().getBankAccount();

        double senderBalance = sender.getBalance();

        User receiverUser = repository.findByAccountNumber(
                Integer.parseInt(
                        request.getReceiverAccountNumber()));

        if (receiverUser == null) {

            return new TransferResponse(
                    false,
                    "Receiver account does not exist.",
                    senderBalance);

        }

        BankAccount receiver =
                receiverUser.getBankAccount();

        if (sender.getAccountNumber()
                .equals(receiver.getAccountNumber())) {

            return new TransferResponse(
                    false,
                    "Cannot transfer money to your own account.",
                    senderBalance);

        }

        if (!AmountRules.isValid(request.getAmount())) {

            return new TransferResponse(
                    false,
                    "Invalid transfer amount.",
                    senderBalance);

        }

        if (!BalanceRules.hasSufficientBalance(
                senderBalance,
                request.getAmount())) {

            return new TransferResponse(
                    false,
                    "Insufficient Balance.",
                    senderBalance);

        }

        double receiverBalance =
                receiver.getBalance() + request.getAmount();

        if (!BalanceRules.withinMaximumLimit(
                receiverBalance)) {

            return new TransferResponse(
                    false,
                    "Receiver account exceeds maximum balance.",
                    senderBalance);

        }

        if (!PinRules.isValid(request.getPin())) {
            return new TransferResponse(
                    false,
                    "PIN must contain 4 or 6 digits.",
                    senderBalance);
        }

        if (!sender.verifyPin(request.getPin())) {

            return new TransferResponse(
                    false,
                    "Incorrect PIN.",
                    senderBalance);

        }

        return new TransferResponse(
                true,
                "Validation successful.",
                senderBalance);

    }

}
