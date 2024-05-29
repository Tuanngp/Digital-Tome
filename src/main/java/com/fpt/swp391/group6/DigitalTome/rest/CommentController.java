package com.fpt.swp391.group6.DigitalTome.rest;


import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {

    private BookService bookService;
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(BookService bookService, CommentService commentService, UserService userService) {

    }

}
