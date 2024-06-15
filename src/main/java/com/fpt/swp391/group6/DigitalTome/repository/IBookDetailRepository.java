package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookDetailRepository {
    List<BookEntity> findByStatus(int status, Pageable pageable);
    BookEntity findByIsbnIncludeCategoriesAndAuthors(String isbn);
    Page<BookEntity> search(SearchRequest searchRequest, Pageable pageable);
}
