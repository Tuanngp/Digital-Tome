package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRest {
    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookEntity>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @GetMapping("/{id}")
    public BookEntity getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
}
