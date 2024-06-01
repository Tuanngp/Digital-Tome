//package com.fpt.swp391.group6.DigitalTome.api;
//import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
//import com.fpt.swp391.group6.DigitalTome.service.CommentService;
//import com.fpt.swp391.group6.DigitalTome.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentApi {
//    private final UserService userService;
//    private final CommentService commentService;
//
//    @Autowired
//    public CommentApi(UserService userService, CommentService commentService) {
//        this.userService = userService;
//        this.commentService = commentService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CommentEntity>> getAllComments() {
//        return ResponseEntity.ok(commentService.getAllComments());
//    }
//
//    @PostMapping
//    public ResponseEntity<CommentEntity> createComment(CommentEntity comment, Principal principal) {
//        comment.setAccountEntity(userService.findByUsername(principal.getName()));
//        CommentEntity savedComment = commentService.saveComment(comment);
//        return ResponseEntity.ok(savedComment);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CommentEntity> getNoteById(@PathVariable(value = "id") Long commentId) {
//        CommentEntity comment = commentService.getById(commentId);
//        return ResponseEntity.ok(comment);
//    }
//
//    @GetMapping(value = "/test")
//    public ResponseEntity<String> test() {
//        StringBuilder html = new StringBuilder();
//        List<CommentEntity> comments = commentService.getAllComments();
//        for (CommentEntity comment : comments) {
//            if (comment.getParentCommentId() == null) {
//                appendCommentHtml(html, comment, 0);
//                parser(comment.getId(), 1, html);
//            }
//        }
//        return ResponseEntity.ok(html.toString());
//    }
//
//    private void parser(Long parentId, int level, StringBuilder html) {
//        List<CommentEntity> comments = commentService.getReplies(parentId);
//        for (CommentEntity comment : comments) {
//            appendCommentHtml(html, comment, level);
//            parser(comment.getId(), level + 1, html);
//        }
//    }
//
//    private void appendCommentHtml(StringBuilder html, CommentEntity comment, int level) {
//        String marginLeft = level > 0 ? "style='margin-left:" + (level * 48) + "px'" : "";
//        html.append("<div class='comment-body' ").append(marginLeft).append(">")
//                .append("<div class='comment-author vcard'>")
//                .append("<img alt='' class='avatar' src='").append(comment.getAccountEntity().getAvatarPath()).append("' style='width: 85px; height: 85px; border-radius: 10%;'/>")
//                .append("<cite class='fn'>").append(comment.getAccountEntity().getFullname()).append("</cite> <span class='says'>says:</span>")
//                .append("<div class='comment-meta'>")
//                .append("<a href='javascript:void(0);'>").append(comment.getModifiedDate()).append("</a>")
//                .append("</div></div>")
//                .append("<div class='comment-content dlab-page-text'>")
//                .append("<p>").append(comment.getContent()).append("</p>")
//                .append("</div>")
//                .append("<div class='panel-footer'>")
//                .append("<button class='btn btn-link reply' type='button' id='").append(comment.getId()).append("'>")
//                .append("<span class='me-2'><i class='fa fa-reply'></i></span>Reply")
//                .append("</button>")
//                .append("</div></div>");
//    }
//}