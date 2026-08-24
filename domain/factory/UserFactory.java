package com.springbootbanking.domain.factory;


import versions.v2.contract.request.RegistrationRequest;
import versions.v2.domain.model.BankAccount;
import versions.v2.domain.model.User;
import versions.v2.utility.IdGenerator;

import java.time.LocalDate;

/*
=========================================================
Class : UserFactory

Layer
Factory Layer

Responsibility
Creates fully initialized User objects.

Design Pattern
Factory Pattern

Why it exists
Encapsulates object creation so that the
Service Layer does not need to know how
User and BankAccount objects are built.

Collaborates With

RegistrationRequest

BankAccount

User

IdGenerator

Version
2.0
=========================================================
*/

public class UserFactory {

    public User create(
            RegistrationRequest request) {

        Integer userId =
                IdGenerator.generateUserId();

        Integer accountNumber =
                IdGenerator.generateAccountNumber();

        BankAccount bankAccount =
                new BankAccount(
                        accountNumber,
                        request.getFullName(),
                        request.getPin());

        return new User(
                userId,
                request.getUsername(),
                request.getPassword(),
                request.getFullName(),
                bankAccount,
                LocalDate.parse(request.getDateOfBirth()));

    }

}
