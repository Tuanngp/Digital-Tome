package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRest {
    private final UserService userService;
    private final CommentService commentService;
    @Autowired
    public CommentRest(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<CommentEntity> createComment(@AuthenticationPrincipal OAuth2User OAuth, CommentEntity comment, Principal principal) {
        if (principal == null) {
            comment.setAccountEntity(userService.findByUsername(OAuth.getAttribute("email")));
        } else {
            comment.setAccountEntity(userService.findByUsername(principal.getName()));
        }
        CommentEntity savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(@PathVariable Long id, CommentEntity updatedComment) {
        CommentEntity comment = commentService.updateComment(id, updatedComment);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentEntity> getNoteById(@PathVariable(value = "id") Long commentId) {
        CommentEntity comment = commentService.getById(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test() {
        StringBuilder htmlBuilder = new StringBuilder();
        List<CommentEntity> comments = commentService.getAllComments();
        for (CommentEntity comment : comments) {
            if (comment.getParentCommentId() == null) {
                buildCommentHtml(htmlBuilder, comment, 0, true);
            }
        }
        return ResponseEntity.ok(htmlBuilder.toString());
    }

    private void buildCommentHtml(StringBuilder htmlBuilder, CommentEntity comment, int level, boolean root) {
        if (!root) {
            appendCommentHtml(htmlBuilder, comment, level);
        }
        List<CommentEntity> replies = commentService.getReplies(comment.getId());
        if (!replies.isEmpty()) {
            for (CommentEntity reply : replies) {
                buildCommentHtml(htmlBuilder, reply, level + 1, false);
            }
        }
    }

    private void appendCommentHtml(StringBuilder htmlBuilder, CommentEntity comment, int level) {
        htmlBuilder.append("<div class='comment-body row' style='margin-left:").append(level * 48).append("px'>")
                .append("<div class='comment-author vcard col-sm-10'>")
                .append("<img alt='' class='avatar' src='").append(comment.getAccountEntity().getAvatarPath()).append("' style='width: 85px; height: 85px; border-radius: 10%;'/>")
                .append("<cite class='fn'>").append(comment.getAccountEntity().getFullname()).append("</cite> <span class='says'>says:</span>")
                .append("<div class='comment-metaa'><a href='javascript:void(0);'>").append(comment.getModifiedDate()).append("</a></div>")
                .append("</div>")
                .append("<div class='dropdown col-sm-2'>")
                .append("<button type='button' class='btn btn-link light light sharp' data-bs-toggle='dropdown'>")
                .append("<svg width='15px' height='15px' viewBox='0 0 24 24' version='1.1'><g stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'><rect x='0' y='0' width='24' height='24'/><circle fill='#000000' cx='5' cy='12' r='2'/><circle fill='#000000' cx='12' cy='12' r='2'/><circle fill='#000000' cx='19' cy='12' r='2'/></g></svg>")
                .append("</button>")
                .append("<div class='dropdown-menu dropdown-menu-end'>")
                .append("<a class='dropdown-item edit' id=").append(comment.getId()).append(">Edit</a>")
                .append("<a class='dropdown-item delete' id=").append(comment.getId()).append(">Delete</a>")
                .append("</div>")
                .append("</div>")
                .append("<div class='comment-content dlab-page-text col-sm-12'>")
                .append("<p>").append(comment.getContent()).append("</p>")
                .append("</div>")
                .append("<div class='panel-footer'>")
                .append("<button class='btn btn-link reply' type='button' id=").append(comment.getId()).append(">")
                .append("<span class='me-2'><i class='fa fa-reply'></i></span>Reply")
                .append("</button>")
                .append("</div>")
                .append("</div>");
    }
}