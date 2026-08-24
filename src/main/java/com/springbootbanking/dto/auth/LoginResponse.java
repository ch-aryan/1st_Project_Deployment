package com.springbootbanking.dto.auth;

public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private String username;
    private Integer accountNumber;
    private boolean registrationComplete;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tokenType, String username, Integer accountNumber, boolean registrationComplete) {
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.accountNumber = accountNumber;
        this.registrationComplete = registrationComplete;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isRegistrationComplete() {
        return registrationComplete;
    }

    public void setRegistrationComplete(boolean registrationComplete) {
        this.registrationComplete = registrationComplete;
    }
}

