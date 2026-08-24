package com.springbootbanking.domain.validator.rules;

public final class AmountRules {

    private static final double MIN_AMOUNT = 0;

    private AmountRules() {
    }

    public static boolean isValid(double amount) {

        return amount > MIN_AMOUNT;

    }

}