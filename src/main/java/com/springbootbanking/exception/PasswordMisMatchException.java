// src/main/java/com/springbootbanking/exception/PasswordMismatchException.java
package com.springbootbanking.exception;

public class PasswordMisMatchException extends RuntimeException {

    public PasswordMisMatchException(String message) {
        super(message);
    }

    public PasswordMisMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}