package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.CommentDto;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.CommentMapper;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.ContentModeratorService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentRest {
    private final CommentService commentService;

    @Autowired
    public CommentRest(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countComment(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(commentService.getCommentsByBookId(id).size());
    }

    @PostMapping("/{bookId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable Long bookId, CommentDto comment) throws IOException {
        return commentService.createComment(bookId, comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "id") Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(@PathVariable(value = "id") Long id, CommentDto updatedComment) {
        return commentService.updateComment(id, updatedComment);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<String> showComment(
            @PathVariable(value = "bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return commentService.showComment(bookId, page, size);
    }

    @GetMapping("all")
    public ResponseEntity<List<CommentEntity>> getAllComment() {
        return commentService.getAllComment();
    }
}
