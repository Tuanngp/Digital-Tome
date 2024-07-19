package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    private final BookService bookService;
    private final AdsService adsService;
    public HomeController(BookService bookService, AdsService adsService) {
        this.bookService = bookService;
        this.adsService = adsService;
    }

    // api to get list book recommendation
    @GetMapping("/api/recommendation")
    @ResponseBody
    public ResponseEntity<List<BookEntity>> getRecommendation() {
        return ResponseEntity.ok(bookService.getRecommendation());
    }

    @GetMapping(value = {"/","home", "index"})
    public String defaultHome(Model model) {

        return "landing-page/index";
    }

    @GetMapping("/payment/premium")
    public String upgrade() {
        return "payment/premium";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "landing-page/about-us";
    }

    @GetMapping("/contact-us")
    public String contact() {
        return "landing-page/contact-us";
    }

    @GetMapping("/blog-detail")
    public String blogDetail() {
        return "blog/blog-detail";
    }

    @GetMapping("/blog-grid")
    public String blogGrid() {
        return "blog/blog-grid";
    }

    @GetMapping("/blog-list-sidebar")
    public String blogListSidebar() {
        return "blog/blog-list-sidebar";
    }

    @GetMapping("/blog-large-sidebar")
    public String blogLargeSidebar() {
        return "blog/blog-large-sidebar";
    }

    @GetMapping("/books-detail")
    public String booksDetail() {
        return "book-view/books-detail";
    }

    @GetMapping("/books-grid-view-sidebar")
    public String booksGridViewSidebar() {
        return "book-view/books-grid-view-sidebar";
    }

    @GetMapping("/deeply-books-seeking")
    public String deeplyBooksSeeking() {
        return "book-view/deeply-books-seeking";
    }

    @GetMapping("/books-list")
    public String booksList() {
        return "book-view/books-list";
    }

    @GetMapping("/books-list-view-sidebar")
    public String booksListViewSidebar() {
        return "book-view/books-list-view-sidebar";
    }

    @GetMapping("/coming-soon")
    public String comingSoon() {
        return "landing-page/coming-soon";
    }

    @GetMapping("/faq")
    public String faq() {
        return "landing-page/faq";
    }

    @GetMapping("/shop-cart")
    public String shopCart() {
        return "shop-cart";
    }

    @GetMapping("/wishlist")
    public String shopWishlist() {
        return "wishlist";
    }

    @GetMapping("/pricing")
    public String pricing() {
        return "landing-page/pricing";
    }

    @GetMapping("/under-construction")
    public String underConstruction() {
        return "error/under-construction";
    }

    @GetMapping("/check-out")
    public String home() {
        return "shop-checkout";
    }

}
