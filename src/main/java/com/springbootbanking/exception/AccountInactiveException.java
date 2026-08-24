// src/main/java/com/springbootbanking/exception/AccountInactiveException.java
package com.springbootbanking.exception;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException(String message) {
        super(message);
    }
}