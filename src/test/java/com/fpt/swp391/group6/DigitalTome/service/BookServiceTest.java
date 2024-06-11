package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.repository.IBookDetailRepository;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private IBookDetailRepository bookRepo;



    @Test
    public void testGetBookDetail() {
        Long bookId = 1L; // Use an existing book ID
        BookDetailDto bookDetailDto = bookService.findByIsbnIncludeCategoriesAndAuthors("978-1-23-456789-8");
        Assertions.assertThat(bookDetailDto).isNotNull();
        Assertions.assertThat(bookDetailDto.getAuthors()).isNotEmpty();
        Assertions.assertThat(bookDetailDto.getCategories()).isNotEmpty();
    }

    @Test
    public void testShowBookDetailList() {
        List<BookDetailDto> list = bookService.findByStatus(2, PageRequest.of(2,5));
        Assertions.assertThat(bookService.countBookByStatus(2)).isEqualTo(20);
        list.forEach(System.out::println);
        Assertions.assertThat(list).isNotEmpty();
    }

    @Test
    public void testSearchBookDetail() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword("Book");
        List<Integer>  years = new ArrayList<>();
        years.add(2016);
        List<String>  categories = new ArrayList<>();
        categories.add("Tam Ly");
        searchRequest.setMinPoint(0);
        searchRequest.setCategories(categories);
        Page<BookDetailDto> list = bookService.search(searchRequest, PageRequest.of(0,5));
        Assertions.assertThat(list).isNotEmpty();
        list.getContent().forEach(System.out::println);
    }


    @Test void findById(){
        BookEntity book = bookService.findByIsbn("978-3-16-148410-0");
        Assertions.assertThat(book).isNotNull();
        System.out.println(book.toString());
    }


}