package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class EmailErrorException extends RuntimeException{
    public EmailErrorException(String message) {
        super(message);
    }

    public EmailErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
