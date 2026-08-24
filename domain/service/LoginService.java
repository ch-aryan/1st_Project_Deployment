package com.springbootbanking.domain.service;

import versions.v2.contract.request.LoginRequest;
import versions.v2.contract.response.LoginResponse;
import versions.v2.domain.model.User;
import versions.v2.domain.validator.LoginValidator;
import versions.v2.repository.UserRepository;

public class LoginService {

    private final UserRepository repository;

    private final LoginValidator validator;

    public LoginService(
            UserRepository repository,
            LoginValidator validator) {

        this.repository = repository;
        this.validator = validator;
    }

    public LoginResponse login(LoginRequest request) {

        LoginResponse validationResult =
                validator.validate(request);

        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        User user = repository.verifyCredentials(
                request.getUsername(),
                request.getPassword());

        if (user == null) {
            return new LoginResponse(
                    false,
                    "Invalid userName or password",
                    null);
        }

        return new LoginResponse(
                true,
                "login successfull",
                user);
    }

}
