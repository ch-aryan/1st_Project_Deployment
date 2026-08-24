package com.springbootbanking.dto.registration;



public class RegistrationResponse {
    private boolean success;
    private String message;
    private Integer accountNumber;


    // Constructors
    public RegistrationResponse() {}

    public RegistrationResponse(boolean success, String message, Integer accountNumber) {
        this.success = success;
        this.message = message;
        this.accountNumber = accountNumber;

    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getAccountNumber() {
        return accountNumber;
    }
}