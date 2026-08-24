package com.springbootbanking.domain.validator.rules;

public final class BalanceRules {

    private static final double MAX_BALANCE = 1_000_000;

    private BalanceRules() {
    }

    public static boolean hasSufficientBalance(
            double balance,
            double amount) {

        return balance >= amount;

    }

    public static boolean withinMaximumLimit(
            double balanceAfterDeposit) {

        return balanceAfterDeposit <= MAX_BALANCE;

    }

}