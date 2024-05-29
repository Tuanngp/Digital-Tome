package com.fpt.swp391.group6.DigitalTome.rest;


import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.Book.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.CommentService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    private BookService bookService;
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(BookService bookService, CommentService commentService, UserService userService) {
        this.bookService = bookService;
        this.commentService = commentService;
        this.userService = userService;
    }

//    @GetMapping("/{bookId}")
//    public String getBook(@PathVariable Long bookId, Model model, Principal principal) {
//        Optional<BookEntity> bookEntity = bookService.getBookById(bookId);
//        List<CommentEntity> commentEntityList = commentService.getCommentByBookId(bookId);
//        if (bookEntity.isPresent()) {
//            model.addAttribute("book", bookEntity.get());
//            model.addAttribute("comments", commentEntityList);
//            model.addAttribute("newComment", new CommentEntity());
//            model.addAttribute("user", principal != null ? userService.findByUsername(principal.getName()) : null);
//            return "book";
//        }
//        return "redirect:/error"; //sach không tồn tại
//    }


//    @PostMapping("comment/")
//    public String addComent(@PathVariable Long bookId,
//                            @ModelAttribute CommentEntity newComment,
//                            Principal principal){
//        if(principal == null){
//            return "redirect:/login";
//        }
//        Optional<BookEntity> bookEntity = bookService.getBookById(bookId);
//        AccountEntity user = userService.findByUsername(principal.getName());
//        newComment.setBookEntity(bookEntity.get());
//        newComment.setAccountEntity(user);
//        commentService.addComment(newComment);
//
//        return "redirect:/books/" + bookId;
//    }
}
