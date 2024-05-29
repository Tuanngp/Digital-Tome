package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.Book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsBookEntityByIsbn(String isbn);

    @Query("SELECT b FROM BookEntity b WHERE DATE(b.createdDate) = CURRENT_DATE")
    List<BookEntity> findBooksCreatedToday();



    Optional<BookEntity> findById(Long id);

    boolean existsByIsbn(String isbn);
    BookEntity findByIsbn(String isbn);
}
