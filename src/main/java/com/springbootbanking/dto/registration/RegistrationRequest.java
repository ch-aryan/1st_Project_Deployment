package com.springbootbanking.dto.registration;

import com.springbootbanking.entity.Gender;
import jakarta.validation.constraints.*;


import java.time.LocalDate;

public class RegistrationRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can contain only letters, numbers and underscore")
    @NotBlank(message = "UserName is required")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 8, max = 64)
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotNull(message = "dob is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(min = 4, max = 4, message = "PIN must be exactly 4 digits")
    @Pattern(regexp = "\\d{4}", message = "PIN must contain exactly 4 digits")
    @NotBlank(message = "pin is required")
    private String pin;

    public RegistrationRequest(String fullName, LocalDate dateOfBirth, Gender gender, String username, String password, String confirmPassword, String pin) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPin() {
        return pin;
    }
}
