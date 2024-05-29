package com.fpt.swp391.group6.DigitalTome.repository;


import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByBookEntityId(Long bookId);





}
