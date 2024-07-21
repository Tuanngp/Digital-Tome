package com.fpt.swp391.group6.DigitalTome.exception.exceptionModel;

import org.springframework.http.HttpStatus;

public class EmailException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public EmailException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }
}
