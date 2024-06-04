package com.fpt.swp391.group6.DigitalTome.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/404")
    public String error404(){
        return "error/error404";
    }
}

