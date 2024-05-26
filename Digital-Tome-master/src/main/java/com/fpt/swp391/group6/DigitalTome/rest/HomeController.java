package com.fpt.swp391.group6.DigitalTome.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/"})
    public String home1() {
        return "index";
    }
    @GetMapping(value = { "/home"})
    public String home2() {
        return "index-2";
    }
    @GetMapping("/login")
    public String login() {
        return "shop-login";
    }

    @GetMapping("/register")
    public String register() {
        return "shop-registration";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contact() {
        return "contact-us";
    }

    @GetMapping("/blog-detail")
    public String blogDetail() {
        return "blog-detail";
    }

    @GetMapping("/blog-grid")
    public String blogGrid() {
        return "blog-grid";
    }

    @GetMapping("/blog-list-sidebar")
    public String blogListSidebar() {
        return "blog-list-sidebar";
    }

    @GetMapping("/blog-large-sidebar")
    public String blogLargeSidebar() {
        return "blog-large-sidebar";
    }

    @GetMapping("/books-detail")
    public String booksDetail() {
        return "books-detail";
    }

    @GetMapping("/books-grid-view")
    public String booksGridView() {
        return "books-grid-view";
    }

    @GetMapping("/books-grid-view-sidebar")
    public String booksGridViewSidebar() {
        return "books-grid-view-sidebar";
    }

    @GetMapping("/books-list")
    public String booksList() {
        return "books-list";
    }

    @GetMapping("/books-list-view-sidebar")
    public String booksListViewSidebar() {
        return "books-list-view-sidebar";
    }

    @GetMapping("/coming-soon")
    public String comingSoon() {
        return "coming-soon";
    }

    @GetMapping("/error")
    public String error() {
        return "error-404";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("index-2")
    public String index2() {
        return "index-2";
    }

    @GetMapping("/shop-cart")
    public String shopCart() {
        return "shop-cart";
    }

    @GetMapping("/shop-checkout")
    public String shopCheckout() {
        return "shop-checkout";
    }

    @GetMapping("/wishlist")
    public String shopWishlist() {
        return "wishlist";
    }

    @GetMapping("/my-profile")
    public String myProfile() {
        return "my-profile";
    }

    @GetMapping("/pricing")
    public String pricing() {
        return "pricing";
    }

    @GetMapping("/under-construction")
    public String underConstruction() {
        return "under-construction";
    }


}
