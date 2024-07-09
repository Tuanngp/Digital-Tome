package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.AuthorEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import jakarta.transaction.Transactional;
import org.hibernate.sql.Update;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsBookEntityByIsbn(String isbn);

    @Query("SELECT b FROM BookEntity b WHERE DATE(b.createdDate) = CURRENT_DATE")
    List<BookEntity> findBooksCreatedToday();


    Optional<BookEntity> findById(Long id);

    boolean existsByIsbn(String isbn);
    BookEntity findByIsbn(String isbn);

    @Transactional
    @Modifying
    @Query("UPDATE BookEntity b SET b.status = :status WHERE b.isbn = :isbn")
    void updateStatusByIsbn(@Param("status") int status,@Param("isbn") String isbn);

    int countByStatus(int status);


}
