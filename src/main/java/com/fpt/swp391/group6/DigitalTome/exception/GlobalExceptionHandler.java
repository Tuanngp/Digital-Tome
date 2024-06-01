//package com.fpt.swp391.group6.DigitalTome.exception;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
//        return "redirect:/error";
//    }
//}
//
