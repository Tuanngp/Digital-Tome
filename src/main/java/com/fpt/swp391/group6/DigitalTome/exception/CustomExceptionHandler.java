package com.fpt.swp391.group6.DigitalTome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorRespone handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ErrorRespone(HttpStatus.NOT_FOUND ,ex.getMessage());
    }
}
