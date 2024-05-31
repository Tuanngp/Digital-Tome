package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.exception.ResourceNotFoundException;
import com.fpt.swp391.group6.DigitalTome.repository.CommentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController2 {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentController2(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/comments")
    @ResponseBody
    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }

    @PostMapping("/comments")
    @ResponseBody
    public CommentEntity createComment(CommentEntity comment) {
//        comment.setAccountEntity(userRepository.findByUsername(principal.getName()));
        return commentRepository.save(comment);
    }

    @GetMapping("/comments/{id}")
    public CommentEntity getNoteById(@PathVariable(value = "id") Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Note"));
    }

    private String html = "";

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        html = "";
        
        List<CommentEntity> comments = commentRepository.findAll();
        for (CommentEntity c : comments) {
            if (c.getParentCommentId() == null) {
                html += "<div class='panel panel-primary'><div class='panel-heading'>By <b>" + c.getAccountEntity().getFullname() +"</b" +
                        "></div>" +
                        "<div class='panel-body'>" + c.getContent() + "</div><div class='panel-footer' align='right'>" +
                        "<button type='button' class='btn btn-primary reply' id=" + c.getId() + ">Reply</button></div" +
                        "> </div>";
                parser(c.getId(), 0, true);
            }
        }
        return html;
    }

    public void parser(Long parentId, int level, boolean root) {
        if (!root) {
            CommentEntity comment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Note"));

            html += "<div class='panel panel-primary' style='margin-left:" + level * 48 + "px'>" +
                    "<div class='panel-heading'>By <b>" + comment.getAccountEntity().getFullname() +"</b></div>" +
                    "<div class='panel-body'>" + comment.getContent() + "</div><div class='panel-footer' " +
                    "align='right'>" +
                    "<button type='button' class='btn btn-primary reply' id=" + comment.getId() + ">Reply</button" +
                    "></div> </div>";
        }

        List<CommentEntity> comments = commentRepository.findByParentCommentId(parentId);
        if (!comments.isEmpty()) {
            for (CommentEntity comment : comments) {
                parser(comment.getId(), level + 1, false);
            }
        }
    }
}