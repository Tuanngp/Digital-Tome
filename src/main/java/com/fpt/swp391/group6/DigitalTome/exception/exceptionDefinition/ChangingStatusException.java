package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class ChangingStatusException extends RuntimeException{
    public ChangingStatusException(String message) {
        super(message);
    }

    public ChangingStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
