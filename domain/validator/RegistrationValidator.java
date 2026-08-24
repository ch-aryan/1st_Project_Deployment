package com.springbootbanking.domain.validator;

import versions.v2.contract.request.RegistrationRequest;
import versions.v2.contract.response.RegistrationResponse;
import versions.v2.domain.validator.rules.NameRules;
import versions.v2.domain.validator.rules.PasswordRules;
import versions.v2.domain.validator.rules.DateOfBirthRules;
import versions.v2.domain.validator.rules.PinRules;
import versions.v2.domain.validator.rules.UsernameRules;
import versions.v2.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RegistrationValidator {

    public RegistrationResponse validate(
            RegistrationRequest request,
            UserRepository repository) {

        if (!NameRules.isValid(request.getFullName())) {
            return new RegistrationResponse(
                    false,
                    "Enter proper name",
                    null);
        }

        if (!hasValidDateOfBirth(request.getDateOfBirth())) {
            return new RegistrationResponse(
                    false,
                    "Enter a valid date of birth.",
                    null);
        }

        if (!UsernameRules.isValid(request.getUsername())) {
            return new RegistrationResponse(
                    false,
                    "please set userName properly",
                    null);
        }

        if (repository.userExists(request.getUsername())) {
            return new RegistrationResponse(
                    false,
                    "userName already exist please" +
                            "select another one",
                    null);
        }

        if (!PasswordRules.isValid(request.getPassword())) {
            return new RegistrationResponse(
                    false,
                    "please set proper password",
                    null);
        }

        if (!PinRules.isValid(request.getPin())) {
            return new RegistrationResponse(
                    false,
                    "PIN must contain 4 or 6 digits.",
                    null);
        }

        return new RegistrationResponse(
                true,
                "Validation successful.",
                null);

    }

    private boolean hasValidDateOfBirth(String dateOfBirth) {

        try {
            return DateOfBirthRules.isValid(
                    LocalDate.parse(dateOfBirth));
        } catch (DateTimeParseException exception) {
            return false;
        }

    }

}
