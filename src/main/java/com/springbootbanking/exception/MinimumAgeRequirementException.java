// src/main/java/com/springbootbanking/exception/MinimumAgeRequirementException.java
package com.springbootbanking.exception;

public class MinimumAgeRequirementException extends RuntimeException {

    public MinimumAgeRequirementException(String message) {
        super(message);
    }

    public MinimumAgeRequirementException(String message, Throwable cause) {
        super(message, cause);
    }
}