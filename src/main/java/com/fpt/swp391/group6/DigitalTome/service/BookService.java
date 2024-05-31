package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.BookDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.BookMapper;
import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;


    public void saveBook(BookDto bookDto){
        boolean existsIsbn = bookRepository.existsBookEntityByIsbn(bookDto.getIsbn());
        if(existsIsbn){
            throw new RuntimeException("The book already exists");
        }
        BookEntity bookEntity = bookMapper.toBook(bookDto);
        bookRepository.save(bookEntity);
    }

//    public Optional<BookEntity> getBookById(Long idBook){
//        Optional<BookEntity> bookEntity = bookRepository.findById(idBook);
//        if(bookEntity == null){
//            throw new RuntimeException("Not found");
//        }
//        bookEntity.get();
//        return bookEntity;
//    }



    public BookEntity getBookById(long id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        BookEntity book =null;
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        } else {
            throw new RuntimeException("Book not found for id : " + id);
        }
        return book;
    }



    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public void saveBook(BookEntity book){
        this.bookRepository.save(book);
    }



    public void deleteBookById(long id){
        this.bookRepository.deleteById(id);
    }

    public Page<BookEntity> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.bookRepository.findAll(pageable);
    }
    public boolean isISBNAlreadyExists(String isbn) {

        return bookRepository.existsByIsbn(isbn);
    }

    public BookEntity findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}

