package com.fpt.swp391.group6.DigitalTome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("censor/contribution")
public class ContributionController {

    @GetMapping
    public String contribution() {
        return "book-validattion/contribution";
    }

    @GetMapping("/rejection")
    public String rejection() {
        return "book-validattion/rejection";
    }

    @GetMapping("/acception")
    public String acception() {
        return "book-validattion/acception";
    }
}
