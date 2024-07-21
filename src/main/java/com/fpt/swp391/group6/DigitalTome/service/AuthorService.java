package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AuthorEntity;
import com.fpt.swp391.group6.DigitalTome.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Optional<AuthorEntity> findById(Long id) {
        return authorRepository.findById(id);
    }
    public List<AuthorEntity> getAllAuthors() {
        return authorRepository.findAll();
    }
    public AuthorEntity save(AuthorEntity author) {
        return authorRepository.save(author);
    }
}