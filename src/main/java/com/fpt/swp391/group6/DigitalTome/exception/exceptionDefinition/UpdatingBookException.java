package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class UpdatingBookException extends RuntimeException{
    public UpdatingBookException(String message) {
        super(message);
    }

    public UpdatingBookException(String message, Throwable cause) {
        super(message, cause);
    }
}