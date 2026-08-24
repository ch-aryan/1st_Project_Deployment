package com.springbootbanking.domain.validator.rules;

import java.time.LocalDate;

public final class DateOfBirthRules {

    private DateOfBirthRules() {
    }

    public static boolean isValid(LocalDate dob) {

        return dob != null;

    }

}