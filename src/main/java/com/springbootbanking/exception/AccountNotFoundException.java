// src/main/java/com/springbootbanking/exception/AccountNotFoundException.java
package com.springbootbanking.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}