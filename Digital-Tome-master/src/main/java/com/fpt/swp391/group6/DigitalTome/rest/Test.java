package com.fpt.swp391.group6.DigitalTome.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Test {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
