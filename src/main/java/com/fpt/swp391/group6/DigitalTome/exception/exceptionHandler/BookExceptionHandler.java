package com.fpt.swp391.group6.DigitalTome.exception.exceptionHandler;

import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.ChangingStatusException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.FindingBookException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.UpdatingBookException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionModel.BookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookExceptionHandler {

    @ExceptionHandler(ChangingStatusException.class)
    public ResponseEntity<Object> handleStatusChange(ChangingStatusException changingStatusException){
        BookException statusChangeException = new BookException(changingStatusException.getMessage(), changingStatusException.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Object>(statusChangeException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdatingBookException.class)
    public ResponseEntity<Object> handleUpdatingBook(UpdatingBookException updatingBookException){
        BookException bookException = new BookException(updatingBookException.getMessage(), updatingBookException.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Object>(bookException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FindingBookException.class)
    public ResponseEntity<Object> handleFindingBook(FindingBookException findingBookException){
        BookException bookException = new BookException(findingBookException.getMessage(), findingBookException.getCause(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<Object>(bookException, HttpStatus.NOT_FOUND);
    }
}
