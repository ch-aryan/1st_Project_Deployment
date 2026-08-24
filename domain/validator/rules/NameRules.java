package com.springbootbanking.domain.validator.rules;

public final class NameRules {

    private NameRules() {
    }

    public static boolean isValid(String fullName) {

        if (fullName == null || fullName.isBlank()) {
            return false;
        }

        return fullName.length() >= 3
                && fullName.length() <= 25;
    }

}