package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    @Autowired
    public CommentController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<CommentEntity> createComment(CommentEntity comment, Principal principal) {
        comment.setAccountEntity(userService.findByUsername(principal.getName()));
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

    private String html = "";

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test() {
        html = "";
        List<CommentEntity> comments = commentService.getAllComments();
        for (CommentEntity comment : comments) {
            if (comment.getParentCommentId() == null) {
                html += "<div class='comment-body row'>" +
                        "   <div class='comment-author vcard col-sm-10'>" +
                        "       <img alt='' class='avatar'" +
                        "             src='" + comment.getAccountEntity().getAvatarPath() + "'" +
                        "             style='width: 85px;" +
                        "                    height: 85px;" +
                        "                    border-radius: 10%;'/>" +
                        "       <cite class='fn'>"+comment.getAccountEntity().getFullname()+"</cite> <span class='says'>says:</span>" +
                        "       <div class='comment-metaa'>" +
                        "           <a href='javascript:void(0);'>"+ comment.getModifiedDate() +"</a>" +
                        "       </div>" +
                        "   </div>" +
                        "   <div class='dropdown col-sm-2'>" +
                        "       <button type='button' class='btn btn-link light light sharp' data-bs-toggle='dropdown'>" +
                        "            <svg width='15px' height='15px' viewBox='0 0 24 24' version='1.1'><g stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'><rect x='0' y='0' width='24' height='24'/><circle fill='#000000' cx='5' cy='12' r='2'/><circle fill='#000000' cx='12' cy='12' r='2'/><circle fill='#000000' cx='19' cy='12' r='2'/></g></svg>" +
                        "       </button>" +
                        "       <div class='dropdown-menu dropdown-menu-end'>" +
                        "           <a class='dropdown-item edit' id="+ comment.getId() + ">Edit</a>" +
                        "           <a class='dropdown-item delete' id="+ comment.getId() + ">Delete</a>" +
                        "       </div>" +
                        "   </div>" +
                        "   <div class='comment-content dlab-page-text col-sm-12'>" +
                        "       <p>"+comment.getContent()+"</p>" +
                        "    </div>" +
                        "    <div class='panel-footer'>" +
                        "        <button class='btn btn-link reply' type='button' id="+ comment.getId() + ">" +
                        "           <span class='me-2'><i class='fa fa-reply'></i></span>Reply" +
                        "        </button>" +
                        "    </div>" +
                        "</div>";
                parser(comment.getId(), 0, true);
            }
        }
        return ResponseEntity.ok(html);
    }

    public void parser(Long parentId, int level, boolean root) {
        if (!root) {
            CommentEntity comment = commentService.getById(parentId);

            html += "<ol class='children'>" +
                    "<div class='comment-body row' style='margin-left:" + level * 48 + "px'>" +
                    "   <div class='comment-author vcard col-sm-10'>" +
                    "       <img alt='' class='avatar'" +
                    "             src='" + comment.getAccountEntity().getAvatarPath() + "'" +
                    "             style='width: 85px;" +
                    "                    height: 85px;" +
                    "                    border-radius: 10%;'/>" +
                    "       <cite class='fn'>"+comment.getAccountEntity().getFullname()+"</cite> <span class='says'>says:</span>" +
                    "       <div class='comment-metaa'>" +
                    "           <a href='javascript:void(0);'>"+ comment.getModifiedDate() +"</a>" +
                    "       </div>" +
                    "   </div>" +
                    "   <div class='dropdown col-sm-2'>" +
                    "       <button type='button' class='btn btn-link light light sharp' data-bs-toggle='dropdown'>" +
                    "            <svg width='15px' height='15px' viewBox='0 0 24 24' version='1.1'><g stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'><rect x='0' y='0' width='24' height='24'/><circle fill='#000000' cx='5' cy='12' r='2'/><circle fill='#000000' cx='12' cy='12' r='2'/><circle fill='#000000' cx='19' cy='12' r='2'/></g></svg>" +
                    "       </button>" +
                    "       <div class='dropdown-menu dropdown-menu-end'>" +
                    "           <a class='dropdown-item edit' id="+ comment.getId() + ">Edit</a>" +
                    "           <a class='dropdown-item delete' id="+ comment.getId() + ">Delete</a>" +
                    "       </div>" +
                    "   </div>" +
                    "   <div class='comment-content dlab-page-text col-sm-12'>" +
                    "       <p>"+comment.getContent()+"</p>" +
                    "    </div>" +
                    "    <div class='panel-footer'>" +
                    "        <button class='btn btn-link reply' type='button' id="+ comment.getId() + ">" +
                    "           <span class='me-2'><i class='fa fa-reply'></i></span>Reply" +
                    "        </button>" +
                    "    </div>" +
                    "</div>" +
                    "</ol>";
        }

        List<CommentEntity> comments = commentService.getReplies(parentId);
        if (!comments.isEmpty()) {
            for (CommentEntity comment : comments) {
                parser(comment.getId(), level + 1, false);
            }
        }
    }
}