package com.fpt.swp391.group6.DigitalTome.exception.exceptionModel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ContributionException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public ContributionException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }
}
