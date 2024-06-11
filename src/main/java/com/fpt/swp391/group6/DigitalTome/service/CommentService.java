package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<CommentEntity> getCommentsByBookId(Long bookId) {
        return commentRepository.findByBookEntityId(bookId);
    }

    public List<CommentEntity> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }

    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    public CommentEntity updateComment(Long id, CommentEntity updatedComment) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            CommentEntity existingComment = optionalComment.get();
            existingComment.setContent(updatedComment.getContent());
            return commentRepository.save(existingComment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    public void deleteComment(Long id) {
        List<CommentEntity> children = commentRepository.findByParentCommentId(id);
        for (CommentEntity child : children) {
            deleteComment(child.getId());
        }
        commentRepository.deleteById(id);
    }

    public Page<CommentEntity> getCommentsByBookId(Long bookId, int page, int size) {
        return commentRepository.findByBookEntityIdOrderByCreatedDateDesc(bookId, PageRequest.of(page, size));
    }
}
