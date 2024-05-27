package com.readbook.ReadBook.repository;


import com.readbook.ReadBook.entity.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findByTitle(String title);

    List<BookEntity> findByStatus(int status);





}
