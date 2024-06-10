package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.CommentDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.CommentMapper;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.ContentModeratorService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentRest {
    private final UserService userService;
    private final CommentService commentService;
    private final BookService bookService;
    private final CommentMapper commentMapper;
    private final ContentModeratorService contentModeratorService;
    @Autowired
    public CommentRest(UserService userService, CommentService commentService, BookService bookService, CommentMapper commentMapper, ContentModeratorService contentModeratorService) {
        this.userService = userService;
        this.commentService = commentService;
        this.bookService = bookService;
        this.commentMapper = commentMapper;
        this.contentModeratorService = contentModeratorService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentEntity> comments = commentService.getAllComments();
        return ResponseEntity.ok(commentMapper.toCommentDto(comments));
    }

    @GetMapping("/count/{id}")
    public ResponseEntity<Integer> countComment(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(commentService.getCommentsByBookId(id).size());
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable Long bookId, CommentDto comment) {
        Map<String, Object> response = new HashMap<>();
        if (contentModeratorService.isContentInappropriate(comment.getContent())) {
            response.put("error", "Content is inappropriate");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        if (contentModeratorService.isSpam(comment.getContent())) {
            response.put("error", "Content is spam");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        AccountEntity account;
        BookEntity book;
        try {
            account = userService.findById(comment.getAccountId());
            book = bookService.getBookById(bookId);
        } catch (Exception e) {
            response.put("error", "Bad request");
            return ResponseEntity.badRequest().body(response);
        }
        comment.setAccountEntity(account);
        comment.setBookEntity(book);
        CommentEntity savedComment = commentService.saveComment(commentMapper.toCommentEntity(comment));
        response.put("comment", savedComment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "id") Long commentId, Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        AccountEntity account = getAccount(principal, oAuth2User);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentEntity comment = commentService.getCommentById(commentId);
        if (comment == null || !comment.getAccountEntity().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(@PathVariable(value = "id") Long id, CommentDto updatedComment, Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        AccountEntity account = getAccount(principal, oAuth2User);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentEntity comment = commentService.getCommentById(id);
        if (comment == null || !comment.getAccountEntity().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        comment = commentService.updateComment(id, commentMapper.toCommentEntity(updatedComment));
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{bookId}")
    @ResponseBody
    public ResponseEntity<String> showComment(
            @PathVariable(value = "bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Principal principal,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) {
        AccountEntity account = getAccount(principal, oAuth2User);
        StringBuilder htmlBuilder = new StringBuilder();
        try {
            Page<CommentEntity> pageComment = commentService.getCommentsByBookId(bookId, page, size);
            List<CommentDto> comments = commentMapper.toCommentDto(pageComment.getContent());
            for (CommentDto comment : comments) {
                if (comment.getParentCommentId() == null) {
                    appendCommentHtml(htmlBuilder, comment, 0, account!=null?account.getId():-1);
                    parser(htmlBuilder, comment, 0, true, account!=null?account.getId():-1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(htmlBuilder.toString());
    }

    private void parser(StringBuilder htmlBuilder, CommentDto comment, int level, boolean root, Long currentAccountId) {
        if (!root) {
            appendCommentHtml(htmlBuilder, comment, level, currentAccountId);
        }
        List<CommentDto> replies = commentMapper.toCommentDto(commentService.getReplies(comment.getId()));
        if (!replies.isEmpty()) {
            for (CommentDto reply : replies) {
                parser(htmlBuilder, reply, level + 1, false, currentAccountId);
            }
        }
    }

    private void appendCommentHtml(StringBuilder htmlBuilder, CommentDto comment, int level, Long currentAccountId) {
        htmlBuilder.append("<div class='comment-body row' style='margin-left:").append(level * 48).append("px'>")
                .append("<div class='comment-author vcard col-sm-10'>")
                .append("<img alt='' class='avatar' src='").append(comment.getAccountEntity().getAvatarPath()).append("' style='width: 85px; height: 85px; border-radius: 10%;'/>")
                .append("<cite class='fn'>").append(comment.getAccountEntity().getFullname()).append("</cite> <span class='says'>says:</span>")
                .append("<div class='comment-metaa'><a href='javascript:void(0);'>").append(comment.getModifiedDate()).append("</a></div>")
                .append("</div>");
        if (comment.getAccountEntity().getId().equals(currentAccountId)) {
            htmlBuilder
                    .append("<div class='dropdown col-sm-2'>")
                    .append("<button type='button' class='btn btn-link light light sharp' data-bs-toggle='dropdown'>")
                    .append("<svg width='15px' height='15px' viewBox='0 0 24 24' version='1.1'><g stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'><rect x='0' y='0' width='24' height='24'/><circle fill='#000000' cx='5' cy='12' r='2'/><circle fill='#000000' cx='12' cy='12' r='2'/><circle fill='#000000' cx='19' cy='12' r='2'/></g></svg>")
                    .append("</button>")
                    .append("<div class='dropdown-menu dropdown-menu-end'>")
                    .append("<a class='dropdown-item edit' id=").append(comment.getId()).append(">Edit</a>")
                    .append("<a class='dropdown-item delete' id=").append(comment.getId()).append(">Delete</a>")
                    .append("</div>")
                    .append("</div>");
        }
        htmlBuilder
                .append("<div class='comment-content dlab-page-text col-sm-12'>")
                .append("<p>").append(comment.getContent()).append("</p>")
                .append("</div>")
                .append("<div class='panel-footer'>")
                .append("<button class='btn btn-link reply' type='button' id=").append(comment.getId()).append(">")
                .append("<span class='me-2'><i class='fa fa-reply'></i></span>Reply")
                .append("</button>")
                .append("</div>")
                .append("<div class='reply-session'></div>")
                .append("</div>");
    }

    private AccountEntity getAccount(Principal principal, OAuth2User oAuth2User) {
        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else if (oAuth2User != null) {
            username = oAuth2User.getAttribute("email");
        }
        return username != null ? userService.findByUsername(username) : null;
    }
}
