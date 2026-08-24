package com.springbootbanking.domain.service;

import versions.v2.contract.request.RegistrationRequest;
import versions.v2.contract.response.RegistrationResponse;
import versions.v2.domain.factory.UserFactory;
import versions.v2.domain.model.User;
import versions.v2.repository.UserRepository;
import versions.v2.domain.validator.RegistrationValidator;

/*
=========================================================
Class : RegistrationService

Layer
Service Layer

Responsibility
Coordinates the complete registration workflow.

Workflow
Receive Request
↓

Validate Request
↓

Create User
↓

Store User
↓

Return Result

Design Principle
Single Responsibility Principle

Architectural Role
Business Orchestrator

Collaborates With

RegistrationValidator

UserFactory

UserRepository

Version
2.0
=========================================================
*/

public class RegistrationService {

    private final UserRepository repository;

    private final RegistrationValidator validator;

    private final UserFactory factory;

    public RegistrationService(
            UserRepository userRepository,
            RegistrationValidator registrationValidator,
            UserFactory userFactory) {

        this.repository = userRepository;
        this.validator = registrationValidator;
        this.factory = userFactory;
    }

    public RegistrationResponse register(
            RegistrationRequest request) {

        RegistrationResponse validationResult =
                validator.validate(
                        request,
                        repository);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        User user =
                factory.create(request);

        repository.storeUser(
                user.getUserName(),
                user);

        return new RegistrationResponse(
                true,
                " registration done successfully",
                user);

    }

}
