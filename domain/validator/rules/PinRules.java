package com.springbootbanking.domain.validator.rules;

public final class PinRules {

    private PinRules() {
    }

    public static boolean isValid(int pin) {

        int length = String.valueOf(pin).length();

        return length == 4 || length == 6;

    }

}