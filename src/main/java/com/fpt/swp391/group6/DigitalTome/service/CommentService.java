package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<CommentEntity> getCommentByBookId(Long id){
        List<CommentEntity> commentEntityList = commentRepository.findByBookEntityId(id);
        if(commentRepository == null){
            throw new RuntimeException("Not found");
        }
        return commentEntityList;
    }

    public CommentEntity addComment (CommentEntity commentEntity){
        return commentRepository.save(commentEntity);
    }

    public void deleteComment (Long id){
        commentRepository.deleteById(id);
    }



}
