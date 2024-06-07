package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.ContentModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ContentModeratorService contentModeratorService;

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody String comment) {

        if ( !commentService.isValidComment(comment)) {
            return ResponseEntity.badRequest().body("Bình luận chứa nội dung không phù hợp!");
        }

        if (commentService.isSpam(comment)) {
            return ResponseEntity.badRequest().body("Bình luận spam!");
        }

        return ResponseEntity.ok("Comment added successfully");
    }


}
