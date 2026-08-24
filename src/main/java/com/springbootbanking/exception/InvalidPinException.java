// src/main/java/com/springbootbanking/exception/InvalidPinException.java
package com.springbootbanking.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super(message);
    }
}