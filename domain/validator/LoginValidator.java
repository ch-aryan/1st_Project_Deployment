package com.springbootbanking.domain.validator;

import versions.v2.contract.request.LoginRequest;
import versions.v2.contract.response.LoginResponse;
import versions.v2.domain.validator.rules.PasswordRules;
import versions.v2.domain.validator.rules.UsernameRules;

public class LoginValidator {

    public LoginResponse validate(LoginRequest request) {

        if (!UsernameRules.isValid(request.getUsername())) {
            return new LoginResponse(
                    false,
                    "Invalid useName",
                    null);
        }

        if (!PasswordRules.isValid(request.getPassword())) {
            return new LoginResponse(
                    false,
                    "Invalid password",
                    null);
        }

        return new LoginResponse(
                true,
                "Validation successful.",
                null);

    }

}