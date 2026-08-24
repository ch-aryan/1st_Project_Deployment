package com.springbootbanking.dto.auth;

import com.springbootbanking.entity.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class OAuthRegistrationCompleteRequest {

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    @Pattern(regexp = "\\d{4}", message = "PIN must contain exactly 4 digits")
    @NotNull(message = "PIN is required")
    private String pin;

    public OAuthRegistrationCompleteRequest() {
    }

    public OAuthRegistrationCompleteRequest(LocalDate dateOfBirth, Gender gender, String pin) {
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.pin = pin;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}

