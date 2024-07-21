package com.fpt.swp391.group6.DigitalTome.exception.exceptionHandler;

import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.ContributionGeneralErrorException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.ContributionNotFoundException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionModel.ContributionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContributionExceptionHandler {

    @ExceptionHandler(ContributionNotFoundException.class)
    public ResponseEntity<Object> handleContributionNotFoundException(ContributionNotFoundException ex) {
        return buildResponseEntity(new ContributionException(ex.getMessage(), ex.getCause(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ContributionGeneralErrorException.class)
    public ResponseEntity<Object> handleGeneralError(ContributionGeneralErrorException ex) {
        return buildResponseEntity(new ContributionException(ex.getMessage(), ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private ResponseEntity<Object> buildResponseEntity(ContributionException ex) {
        return new ResponseEntity<>(ex, ex.getHttpStatus());
    }
}
