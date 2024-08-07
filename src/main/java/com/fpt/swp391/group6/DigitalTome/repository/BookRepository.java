package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsBookEntityByIsbn(String isbn);

    @Query("SELECT b FROM BookEntity b WHERE DATE(b.createdDate) = CURRENT_DATE")
    List<BookEntity> findBooksCreatedToday();

    @Query("SELECT b FROM BookEntity b JOIN b.contributionEntityList c WHERE c.accountEntity.id = :accountId")
    Page<BookEntity> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    Optional<BookEntity> findById(Long id);
    boolean existsByIsbn(String isbn);

    BookEntity findByIsbn(String isbn);

    @Transactional
    @Modifying
    @Query("UPDATE BookEntity b SET b.status = :status WHERE b.isbn = :isbn")
    void updateStatusByIsbn(@Param("status") int status,@Param("isbn") String isbn);

    int countByStatus(int status);


    List<BookEntity> findTop10ByOrderByViews();
}
