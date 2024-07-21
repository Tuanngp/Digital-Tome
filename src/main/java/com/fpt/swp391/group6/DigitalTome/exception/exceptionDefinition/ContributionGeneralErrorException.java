package com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition;

public class ContributionGeneralErrorException extends RuntimeException {
    public ContributionGeneralErrorException(String message) {
        super(message);
    }

    public ContributionGeneralErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
