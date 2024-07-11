package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.FavoriteService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteRest {
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final BookService bookService;

    public FavoriteRest(FavoriteService favoriteService, UserService userService, BookService bookService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("")
    public ResponseEntity<?> getFavorites(
            Pageable pageable,
            Principal principal,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        var user = userService.getCurrentUser(principal, oAuth2User);
        var favoritesPage = favoriteService.getFavoritesByUser(user);
        return ResponseEntity.ok(favoritesPage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add/{bookId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long bookId, Principal principal, @AuthenticationPrincipal OAuth2User oAuth2User) {
        var user = userService.getCurrentUser(principal, oAuth2User);
        var book = bookService.getBookById(bookId);
        if (favoriteService.isFavorite(bookId, user)) {
            return ResponseEntity.badRequest().build();
        }
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setAccountEntity(user);
        favorite.setBookEntity(book);
        favoriteService.addFavorite(favorite);
        return ResponseEntity.noContent().build();
    }
}
