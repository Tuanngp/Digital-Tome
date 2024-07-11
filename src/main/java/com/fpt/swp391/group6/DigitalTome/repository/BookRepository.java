package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AuthorEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsBookEntityByIsbn(String isbn);

    @Query("SELECT b FROM BookEntity b WHERE DATE(b.createdDate) = CURRENT_DATE")
    List<BookEntity> findBooksCreatedToday();

    @Query("SELECT b FROM BookEntity b JOIN b.contributionEntityList c WHERE c.accountEntity.id = :accountId")
    Page<BookEntity> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    Optional<BookEntity> findById(Long id);
    boolean existsByIsbn(String isbn);
    BookEntity findByIsbn(String isbn);
}
