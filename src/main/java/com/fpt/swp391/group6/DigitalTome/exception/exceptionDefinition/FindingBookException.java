package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class FindingBookException extends RuntimeException{
    public FindingBookException(String message) {
        super(message);
    }

    public FindingBookException(String message, Throwable cause) {
        super(message, cause);
    }
}
