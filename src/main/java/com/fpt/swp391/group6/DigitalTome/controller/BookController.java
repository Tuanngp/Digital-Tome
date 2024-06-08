package com.fpt.swp391.group6.DigitalTome.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.swp391.group6.DigitalTome.entity.Author;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.service.AuthorService;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.CategoryService;
import com.fpt.swp391.group6.DigitalTome.utils.BookUtils;
import com.fpt.swp391.group6.DigitalTome.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/books-view")
    public String bookListView(Model model) {
        return findPaginated(1, "id", "asc", model);
    }

    @GetMapping("/books-manage")
    public String bookListManage(Model model) {
        return findPaginatedList(1, "id", "asc", model);
    }

    @GetMapping("/upload")
    public String showAddBookForm( Model model) {
        List<Author> existingAuthors = authorService.getAllAuthors();
        model.addAttribute("book", new BookEntity());
        model.addAttribute("authors", existingAuthors);
        return "books-upload";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookEntity book,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("bookP") MultipartFile bookP,
                           @RequestParam("authorIds") String authorIds,
                           @RequestParam("categoryEntityList") List<Long> categoryIds,
                           Model model) throws JsonProcessingException {
        System.out.println("ID của sách khi cập nhật: " + book.getId());
        if (book.getId() != null) {
            BookEntity existingBook = bookService.findByIsbn(book.getIsbn());
            if (existingBook != null && !existingBook.getId().equals(book.getId())) {
                model.addAttribute("error", "ISBN đã tồn tại!");
                return "book-upload/update-book";
            }
        } else {
            if (bookService.isISBNAlreadyExists(book.getIsbn())) {
                model.addAttribute("error", "ISBN đã tồn tại!");
                return "books-upload";
            }
        }

        // Lưu file ảnh và sách
        if (!image.isEmpty() && !bookP.isEmpty()) {
            try {
                // Upload image to Cloudinary
                String imageUrl = ImageUtils.uploadImage(image);

                // Upload book to Cloudinary
                String bookUrl = BookUtils.uploadBook(bookP);

                // Đặt đường dẫn vào thuộc tính coverBook
                book.setBookCover(imageUrl);

                // Đặt đường dẫn vào thuộc tính bookPath
                book.setBookPath(bookUrl);
            } catch (RuntimeException e) {
                e.printStackTrace();
                model.addAttribute("error", "Đã xảy ra lỗi khi lưu sách!");
                return "books-upload";
            }
        } else {
            model.addAttribute("error", "Vui lòng chọn file để upload!");
            return "books-upload";
        }

        // Xử lý danh sách tác giả
        List<Author> authors = new ArrayList<>();
        String[] authorIdArray = authorIds.split(",");
        for (String authorId : authorIdArray) {
            if (authorId.startsWith("new-")) {
                String authorName = authorId.substring(4);
                Author newAuthor = new Author();
                newAuthor.setName(authorName);
                newAuthor = authorService.save(newAuthor); // Lưu tác giả mới vào cơ sở dữ liệu
                authors.add(newAuthor);
            } else {
                Long id = Long.valueOf(authorId);
                Optional<Author> authorOptional = authorService.findById(id);
                authorOptional.ifPresent(authors::add);
            }
        }
        book.setAuthorEntityList(authors);

        List<CategoryEntity> categories = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            Optional<CategoryEntity> categoryOptional = categoryService.findById(categoryId);
            categoryOptional.ifPresent(categories::add);
        }
        book.setCategoryEntityList(categories);

        // Lưu sách
        bookService.saveBook(book);
        return "redirect:/books-manage";
    }




    @GetMapping("/update/{id}")
    public String showEditBookForm ( @PathVariable(value = "id") long id, Model model){
        List<CategoryEntity> categories = categoryService.getAllCategories();
        BookEntity book = bookService.getBookById(id);
        List<Author> authors = book.getAuthorEntityList();
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("book", book);
        return "book-upload/update-book";
    }


    @GetMapping("/delete/{id}")
    public String deleteBook ( @PathVariable(value = "id") long id){
        this.bookService.deleteBookById(id);
        return "redirect:/books-manage";
    }

    @GetMapping("/books-view/{pageNo}")
    public String findPaginated ( @PathVariable(value = "pageNo") int pageNo,
    @RequestParam("sortField") String sortField,
    @RequestParam("sortDir") String sortDir,
    Model model){
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
    public String findPaginatedList ( @PathVariable(value = "pageNo") int pageNo,
    @RequestParam("sortField") String sortField,
    @RequestParam("sortDir") String sortDir,
    Model model){
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
        return "books-manage";
    }
}

