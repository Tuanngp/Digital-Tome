package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.Author;
import com.fpt.swp391.group6.DigitalTome.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    public Author save(Author author) {
        return authorRepository.save(author);
    }
}