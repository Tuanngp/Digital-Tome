package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

//    @GetMapping("/test-list")
//    public String showBooksList(@RequestParam(defaultValue = "0") int page, Model model) {
//        int pageSize = 16;
//        Page<BookEntity> bookPage = bookService.getPaginatedBooks(page, pageSize);
//        model.addAttribute("bookPage", bookPage);
//        model.addAttribute("currentPage", page);
//        return "test-list";
//    }

    @GetMapping("/books-view")
    public String bookListView(Model model) {
        return findPaginated(1, "id", "asc", model);
    }
    @GetMapping("/books-manage")
    public String bookListManage(Model model) {
        return findPaginatedList(1, "id", "asc", model);
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new  BookEntity());
        return "new-book";
    }

//    @PostMapping("/save")
//    public String saveBook(@ModelAttribute("book") BookEntity book){
//        bookService.saveBook(book);
//        return "redirect:/books-manage";
//    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookEntity book,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("bookP") MultipartFile bookP,
                           Model model) {
        System.out.println("ID cá»§a sÃ¡ch khi cáº­p nháº­t: " + book.getId());
        if (book.getId() != null) {

            BookEntity existingBook = bookService.findByIsbn(book.getIsbn());
            if (existingBook != null && !existingBook.getId().equals(book.getId())) {

                model.addAttribute("error", "ISBN Ä‘Ã£ tá»“n táº¡i 1!");
                return "update-book";
            }
        } else {

            if (bookService.isISBNAlreadyExists(book.getIsbn())) {
                model.addAttribute("error", "ISBN Ä‘Ã£ tá»“n táº¡i!");
                return "new-book";
            }
        }


        if (!image.isEmpty() && !bookP.isEmpty()) {
            try {
                String fileImage = image.getOriginalFilename();
                String imageDir = "Digital-Tome-master/src/main/resources/static/images/books/grid/";


                String fileBook = bookP.getOriginalFilename();
                String bookDir = "books/";


                File imageDirFile = new File(imageDir);
                if (!imageDirFile.exists()) {
                    imageDirFile.mkdirs();
                }


                File bookDirFile = new File(bookDir);
                if (!bookDirFile.exists()) {
                    bookDirFile.mkdirs();
                }


                Path filePathImage = Paths.get(imageDir, fileImage);
                Files.write(filePathImage, image.getBytes());


                Path filePathBook = Paths.get(bookDir, fileBook);
                Files.write(filePathBook, bookP.getBytes());


                String relativeImagePath = "../images/books/grid/" + fileImage;
                book.setBookCover(relativeImagePath);


                String relativeBookPath =  bookDir + fileBook;
                book.setBookPath(relativeBookPath);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "ÄÃ£ xáº£y ra lá»—i khi lÆ°u sÃ¡ch!");
                return "new-book";
            }
        }else {
            model.addAttribute("error", "Vui lÃ²ng chá»n file Ä‘á»ƒ upload!");
            return "new-book";
        }

        bookService.saveBook(book);
        return "redirect:/books-manage";
    }


//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String saveStore(@ModelAttribute("book") BookEntity book,
//                            @RequestParam("bookP") MultipartFile bookP) {
//        if (!bookP.isEmpty()) {
//            try {
//                // ÄÆ°á»ng dáº«n nÆ¡i báº¡n sáº½ lÆ°u hÃ¬nh áº£nh
//                String fileName = bookP.getOriginalFilename();
//                String uploadDir = "books/";
//
//                // Táº¡o thÆ° má»¥c náº¿u chÆ°a tá»“n táº¡i
//                File uploadDirFile = new File(uploadDir);
//                if (!uploadDirFile.exists()) {
//                    uploadDirFile.mkdirs();
//                }
//
//                // LÆ°u tá»‡p hÃ¬nh áº£nh
//                Path filePath = Paths.get(uploadDir, fileName);
//                Files.write(filePath, bookP.getBytes());
//
//                // Äáº·t Ä‘Æ°á»ng dáº«n vÃ o thuá»™c tÃ­nh imagePath cá»§a Store
//                book.setBookPath(filePath.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // LÆ°u thÃ´ng tin Store
//        bookService.saveBook(book);
//
//        return "redirect:/books-manage";
//    }


    @GetMapping("/update/{id}")
    public String showEditBookForm(@PathVariable ( value = "id") long id, Model model) {
        BookEntity book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "update-book";
    }


    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable (value = "id") long id) {
        this.bookService.deleteBookById(id);
        return "redirect:/books-manage";
    }

    @GetMapping("/books-view/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 12;

        Page<BookEntity> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<BookEntity> listBooks = page.getContent();

        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listBooks", listBooks);
        return "books-grid-view";
    }

    @GetMapping("/books-manage/{pageNo}")
    public String findPaginatedList(@PathVariable (value = "pageNo") int pageNo,
                                    @RequestParam("sortField") String sortField,
                                    @RequestParam("sortDir") String sortDir,
                                    Model model) {
        int pageSize = 12;

        Page<BookEntity> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<BookEntity> listBooks = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listBooks", listBooks);
        return "books-list";
    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> handleFileUpload (@RequestParam("file") MultipartFile file) {
//        String filename = file.getOriginalFilename();
//        try {
//            file.transferTo( new File("D:/Java Web/Digital-Tome-main/books" + filename));
//        } catch (Exception e) {
//           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//
//        }
//        return ResponseEntity.ok("file upload thanh cong");
//    }
}
