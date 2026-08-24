package com.springbootbanking.domain.validator.rules;

import java.util.regex.Pattern;

public final class PasswordRules {

    private static final Pattern PATTERN =
            Pattern.compile(
                    "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=\\[]).{8,}$");

    private PasswordRules() {
    }

    public static boolean isValid(String password) {

        if (password == null) {
            return false;
        }

        return PATTERN.matcher(password).matches();

    }

}