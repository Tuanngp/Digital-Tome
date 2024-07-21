package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthorRest {
    private final AuthorService authorService;

    @Autowired
    public AuthorRest(AuthorService authorService) {
        this.authorService = authorService;
    }
}
