package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.dto.CommentDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.CommentMapper;
import com.fpt.swp391.group6.DigitalTome.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookService bookService;
    private final CommentMapper commentMapper;
    private final ContentModeratorService contentModeratorService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, BookService bookService, CommentMapper commentMapper, ContentModeratorService contentModeratorService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.commentMapper = commentMapper;
        this.contentModeratorService = contentModeratorService;
    }

    private CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    private List<CommentEntity> getReplies(Long parentCommentId) {
        return commentRepository.findByParentCommentId(parentCommentId);
    }

    private CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    private CommentEntity updateComment(Long id, CommentEntity updatedComment) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            CommentEntity existingComment = optionalComment.get();
            existingComment.setContent(updatedComment.getContent());
            return commentRepository.save(existingComment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    private void delete(Long id) {
        List<CommentEntity> children = commentRepository.findByParentCommentId(id);
        for (CommentEntity child : children) {
            deleteComment(child.getId());
        }
        commentRepository.deleteById(id);
    }

    private Page<CommentEntity> getCommentsByBookId(Long bookId, int page, int size) {
        return commentRepository.findByBookEntityIdOrderByCreatedDateDesc(bookId, PageRequest.of(page, size));
    }

    public List<CommentEntity> getCommentsByBookId(Long bookId) {
        return commentRepository.findByBookEntityId(bookId);
    }

    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentEntity> comments = commentRepository.findAll();
        return ResponseEntity.ok(commentMapper.toCommentDto(comments));
    }

    public ResponseEntity<Map<String, Object>> createComment(Long bookId, CommentDto comment) throws IOException {
        Map<String, Object> response = new HashMap<>();

        if (isContentInappropriateOrSpam(comment.getContent(), response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<AccountEntity> accountOpt = Optional.ofNullable(userService.findById(comment.getAccountId()));
        Optional<BookEntity> bookOpt = Optional.ofNullable(bookService.getBookById(bookId));
        if (accountOpt.isEmpty() || bookOpt.isEmpty()) {
            response.put("error", "Bad request");
            return ResponseEntity.badRequest().body(response);
        }

        comment.setAccountEntity(accountOpt.get());
        comment.setBookEntity(bookOpt.get());

        CommentEntity savedComment = saveComment(commentMapper.toCommentEntity(comment));
        response.put("comment", savedComment);
        return ResponseEntity.ok(response);
    }

    private boolean isContentInappropriateOrSpam(String content, Map<String, Object> response) throws IOException {
        if (contentModeratorService.isContentInappropriate(content)) {
            response.put("error", "Content is inappropriate");
            return true;
        }
        if (contentModeratorService.isSpam(content)) {
            response.put("error", "Content is spam");
            return true;
        }
        return false;
    }

    public ResponseEntity<Void> deleteComment(Long commentId) {
        AccountEntity account = userService.getCurrentLogin();
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentEntity comment = getCommentById(commentId);
        if (comment == null || !comment.getAccountEntity().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        delete(commentId);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<CommentEntity> updateComment(Long id, CommentDto updatedComment) {
        AccountEntity account = userService.getCurrentLogin();
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentEntity comment = getCommentById(id);
        if (comment == null || !comment.getAccountEntity().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        comment = updateComment(id, commentMapper.toCommentEntity(updatedComment));
        return ResponseEntity.ok(comment);
    }

    public ResponseEntity<String> showComment(Long bookId, int page, int size) {
        AccountEntity account = userService.getCurrentLogin();
        StringBuilder htmlBuilder = new StringBuilder();
        try {
            Page<CommentEntity> pageComment = getCommentsByBookId(bookId, page, size);
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
        List<CommentDto> replies = commentMapper.toCommentDto(getReplies(comment.getId()));
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

    public ResponseEntity<List<CommentEntity>> getAllComment() {
        return ResponseEntity.ok(commentRepository.findAll());
    }
}
