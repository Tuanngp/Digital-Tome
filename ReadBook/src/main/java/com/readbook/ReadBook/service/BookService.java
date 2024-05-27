package com.readbook.ReadBook.service;


import com.readbook.ReadBook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public void saveBook(){

    }



}
