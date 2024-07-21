package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class ContributionNotFoundException extends RuntimeException {
    public ContributionNotFoundException(String message) {
        super(message);
    }

    public ContributionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
