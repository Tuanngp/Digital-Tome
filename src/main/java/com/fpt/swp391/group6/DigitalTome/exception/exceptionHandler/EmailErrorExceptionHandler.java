package com.fpt.swp391.group6.DigitalTome.exception.exceptionHandler;

import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.EmailErrorException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionModel.EmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmailErrorExceptionHandler {
    @ExceptionHandler(EmailErrorException.class)
    public ResponseEntity<Object> handleSendingEmailError(EmailErrorException emailErrorException){
        EmailException emailException = new EmailException(emailErrorException.getMessage(), emailErrorException.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        return  new ResponseEntity<>(emailException,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
