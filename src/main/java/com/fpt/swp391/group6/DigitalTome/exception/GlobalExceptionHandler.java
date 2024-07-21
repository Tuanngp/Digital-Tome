//package com.fpt.swp391.group6.DigitalTome.exception;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(JsonProcessingException.class)
//    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
//        return new ResponseEntity<>("Error processing JSON: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex, Model model, HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//            httpStatus = HttpStatus.valueOf(statusCode);
//        }
//
//        model.addAttribute("errorMessage", ex.getMessage());
//        model.addAttribute("status", httpStatus.value());
//        return "error/under-construction";
//    }
//}
//
