package com.springbootbanking.domain.validator.rules;

import java.util.regex.Pattern;

public final class UsernameRules {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z]\\w{5,29}$");

    private UsernameRules() {
    }

    public static boolean isValid(String username) {

        if (username == null) {
            return false;
        }

        return PATTERN.matcher(username).matches();
    }

}