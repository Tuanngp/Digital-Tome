package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByBookEntityId(Long id);
    List<CommentEntity> findByParentCommentId(Long parentId);
    Page<CommentEntity> findByBookEntityIdOrderByCreatedDateDesc(Long bookId, Pageable pageable);
}