    package com.fpt.swp391.group6.DigitalTome.controller;

    import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
    import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
    import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
    import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
    import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
    import com.fpt.swp391.group6.DigitalTome.service.BookService;
    import com.fpt.swp391.group6.DigitalTome.service.FavoriteService;
    import com.fpt.swp391.group6.DigitalTome.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @Controller
    @RequestMapping("/favorites")
    public class FavoriteController {

        @Autowired
        private FavoriteService favoriteService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BookRepository bookRepository;
        @Autowired
        private BookService bookService;

        @GetMapping("/{userId}")
        public String getFavorites(@PathVariable Long userId, Model model) {
            List<FavoriteEntity> favorites = favoriteService.getFavoritesByUserId(userId);
            model.addAttribute("favorites", favorites);
            return "account/reading-history";
        }


        @PostMapping("/addFavorite")
        public String addFavorite(@RequestParam Long bookId, @RequestParam Long userId) {
            FavoriteEntity favorite = new FavoriteEntity();

            Optional<AccountEntity> accountEntityOpt = userRepository.findById(userId);
            BookEntity bookEntity = bookService.getBookById(bookId);


            if(accountEntityOpt.isPresent()){
                AccountEntity accountEntity = accountEntityOpt.get();

                favorite.setAccountEntity(accountEntity);
                favorite.setBookEntity(bookEntity);

                favoriteService.addFavorite(favorite);
                return "redirect:/favorites/" + userId;
            }else{
                return "redirect:/404";
            }
        }

        @PostMapping("/remove")
        public String removeFavorite(@RequestParam Long favoriteId) {
            favoriteService.removeFavorite(favoriteId);
            return "redirect:/favorites";
        }
    }
