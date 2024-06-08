package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsBookEntityByIsbn(String isbn);


    boolean existsByIsbn(String isbn);
    BookEntity findByIsbn(String isbn);



}
