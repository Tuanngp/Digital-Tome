package com.fpt.swp391.group6.DigitalTome.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.swp391.group6.DigitalTome.dto.CategoryDto;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import com.fpt.swp391.group6.DigitalTome.service.*;
import com.fpt.swp391.group6.DigitalTome.utils.BookUtils;
import com.fpt.swp391.group6.DigitalTome.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final ContributionService contributionService;
    private final PaymentService paymentService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, CategoryService categoryService, AccountService accountService, ContributionService contributionService, PaymentService paymentService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.contributionService = contributionService;
        this.paymentService = paymentService;
    }

    @GetMapping("/books-grid-view")
    public String bookListView(Model model) {
        return findPaginated(1, "id", "asc", model);
    }
    @GetMapping("/books-chart")
    public String getBooksChartPage(@RequestParam(required = false) Long accountId, Model model) {
        model.addAttribute("accountId", accountId);
        return "book-manager/books-chart";
    }

    @GetMapping("/books-manage")
    public String bookListManage(Model model, Principal principal) {
        return findPaginatedList(1, "id", "desc", model, principal, 10);
    }
    @GetMapping("/upload")
    public String showAddBookForm(Model model) {
        List<AuthorEntity> existingAuthors = authorService.getAllAuthors();
        model.addAttribute("book", new BookEntity());
        model.addAttribute("authors", existingAuthors);
        return "book-manager/books-upload";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") BookEntity book,
                           @RequestParam(value = "image", required = false) MultipartFile image,
                           @RequestParam(value = "bookP", required = false) MultipartFile bookP,
                           @RequestParam("authorIds") String authorIds,
                           @RequestParam("categoryEntityList") List<Long> categoryIds,
                           @RequestParam("pricing") double pricing,
                           Principal principal,
                           Model model) throws JsonProcessingException {

        List<AuthorEntity> existingAuthors = authorService.getAllAuthors();
        List<CategoryDto> catego = categoryService.getAllCategories();
        model.addAttribute("authors", existingAuthors);
        model.addAttribute("categories", catego);

        System.out.println("ID của sách khi cập nhật: " + book.getId());

        // Kiểm tra ISBN
        if (book.getId() != null) {
            BookEntity existingBook = bookService.getBookById(book.getId());
            if (existingBook == null) {
                model.addAttribute("error", "Book not found!");
                return "book-manager/update-book";
            }

            // Kiểm tra ISBN trùng lặp
            if (bookService.findByIsbn(book.getIsbn()) != null && !existingBook.getId().equals(book.getId())) {
                model.addAttribute("error", "ISBN already exists in the system!");
                return "book-manager/update-book";
            }

            // Giữ lại giá trị cũ nếu không có tệp mới
            if (image != null && !image.isEmpty()) {
                try {
                    String imageUrl = ImageUtils.uploadImage(image, "image/books/");
                    book.setBookCover(imageUrl);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "An error occurred while saving the image!");
                    return "book-manager/update-book";
                }
            } else {
                book.setBookCover(existingBook.getBookCover());
            }

            if (bookP != null && !bookP.isEmpty()) {
                try {
                    String bookUrl = BookUtils.uploadBook(bookP, "books/");
                    book.setBookPath(bookUrl);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    model.addAttribute("error", "An error occurred while saving the book file!");
                    return "book-manager/update-book";
                }
            } else {
                book.setBookPath(existingBook.getBookPath());
            }
        } else {
            if (bookService.isISBNAlreadyExists(book.getIsbn())) {
                model.addAttribute("error", "ISBN already exists in the system!");
                return "book-manager/books-upload";
            }

            // Xử lý tệp ảnh và sách khi tạo mới
            if (image == null || image.isEmpty()) {
                model.addAttribute("error", "Please select an image file to upload!");
                return "book-manager/books-upload";
            }

            if (bookP == null || bookP.isEmpty()) {
                model.addAttribute("error", "Please select a book file to upload!");
                return "book-manager/books-upload";
            }

            try {
                String imageUrl = ImageUtils.uploadImage(image, "image/books/");
                String bookUrl = BookUtils.uploadBook(bookP, "books/");
                book.setBookCover(imageUrl);
                book.setBookPath(bookUrl);
            } catch (RuntimeException e) {
                e.printStackTrace();
                model.addAttribute("error", "An error occurred while saving the file!");
                return "book-manager/books-upload";
            }
        }

        // Kiểm tra ngày xuất bản
        LocalDate currentDate = LocalDate.now();
        Date publicationDate = book.getPublicationDate();
        if (publicationDate != null) {
            LocalDate localPublicationDate = Instant.ofEpochMilli(publicationDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (localPublicationDate.isAfter(currentDate)) {
                model.addAttribute("error", "The published date must be less than the current date!");
                return "book-manager/books-upload";
            }
        }

        // Xử lý danh sách tác giả
        List<AuthorEntity> authors = new ArrayList<>();
        String[] authorIdArray = authorIds.split(",");
        for (String authorId : authorIdArray) {
            if (authorId.startsWith("new-")) {
                String authorName = authorId.substring(4);
                AuthorEntity newAuthor = new AuthorEntity();
                newAuthor.setName(authorName);
                newAuthor = authorService.save(newAuthor);
                authors.add(newAuthor);
            } else {
                Long id = Long.valueOf(authorId);
                Optional<AuthorEntity> authorOptional = authorService.findById(id);
                authorOptional.ifPresent(authors::add);
            }
        }
        book.setAuthorEntityList(authors);

        // Xử lý danh sách category
        List<CategoryEntity> categories = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            Optional<CategoryEntity> categoryOptional = categoryService.findById(categoryId);
            categoryOptional.ifPresent(categories::add);
        }
        book.setCategoryEntityList(categories);

        // Xử lý contribution
        AccountEntity accountEntity = accountService.findByUsername(principal.getName());
        ContributionEntity contribution = new ContributionEntity();
        contribution.setAccountEntity(accountEntity);
        contribution.setBookEntity(book);
        contribution.setBookCertificate("Book uploaded by " + principal.getName());

        contributionService.saveContribution(contribution);

        // Pricing và set free
        book.setRestricted(pricing > 0);
        book.setPoint((long) pricing);
        book.setStatus(1);

        bookService.saveBook(book);
        return "redirect:/books-manage";
    }



    @GetMapping("/update/{id}")
    public String showEditBookForm(@PathVariable(value = "id") long id, Model model, Principal principal) {
        BookEntity book = bookService.getBookById(id);

        // Check if the logged-in user is the author of the book
        AccountEntity accountEntity = accountService.findByUsername(principal.getName());
        boolean isAuthor = contributionService.isAuthorOfBook(accountEntity.getId(), id);
        if (!isAuthor) {
            return "error/error403";
        }

        List<CategoryDto> categories = categoryService.getAllCategories();
        List<AuthorEntity> authors = book.getAuthorEntityList();
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("book", book);
        System.out.println("Loaded book id for update: " + book.getId());
        return "book-manager/update-book";
    }



    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable(value = "id") long id, Principal principal) {
        // Check if the logged-in user is the author of the book
        AccountEntity accountEntity = accountService.findByUsername(principal.getName());
        boolean isAuthor = contributionService.isAuthorOfBook(accountEntity.getId(), id);
        if (!isAuthor) {
            return "error/error403";
        }

        this.bookService.deleteBookById(id);
        return "redirect:/books-manage";
    }

    @GetMapping("/books-view/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
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
        return "book-view/books-grid-view";
    }

    @GetMapping("/books-manage/{pageNo}")
    public String findPaginatedList(@PathVariable(value = "pageNo") int pageNo,
                                    @RequestParam("sortField") String sortField,
                                    @RequestParam("sortDir") String sortDir,
                                    Model model, Principal principal,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        if (principal == null) {
            return "redirect:/login";
        }
        AccountEntity accountEntity = accountService.findByUsername(principal.getName());
        Page<BookEntity> page = bookService.findPaginatedByAccountId(accountEntity.getId(), pageNo, pageSize, sortField, sortDir);
        List<BookEntity> listBooks = page.getContent();

        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startEntry", (pageNo - 1) * pageSize + 1);
        model.addAttribute("endEntry", Math.min((long) pageNo * pageSize, page.getTotalElements()));

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listBooks", listBooks);
        return "book-manager/books-manage";
    }


    @GetMapping("/books/{isbn}")
    public String showBookDetail(@PathVariable(value = "isbn") String isbn, Principal principal, Model model) {
        BookEntity book = bookService.getBookByIsbn(isbn);
        boolean isOwned = false;

        if (principal != null) {
            AccountEntity user = accountService.findByUsername(principal.getName());
            if (user != null) {
                MembershipEntity membership = user.getMembershipEntity();
                if (membership != null) {
                    isOwned = true;  // User has an upgraded account, can read the book
                } else {
                    isOwned = paymentService.existsByAccountEntityAndBookEntity(user, book);
                }
            }
        }

        model.addAttribute("book", book);
        model.addAttribute("isOwned", isOwned);
        return "book-view/books-detail";
    }

    @PostMapping("/buy-book")
    public String buyBook(@RequestParam("bookId") Long bookId, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        BookEntity book = bookService.getBookById(bookId);
        AccountEntity user = accountService.findByUsername(principal.getName());

        if (user != null && book != null) {
            long userPoints = user.getPoint();
            MembershipEntity membership = user.getMembershipEntity();
            long bookPrice = book.getPoint();

            if (membership == null && userPoints >= bookPrice) {
                // Deduct points from the user
                user.setPoint(userPoints - bookPrice);
                accountService.save(user);

                // Save the transaction in the payment table
                PaymentEntity payment = new PaymentEntity();
                payment.setAccountEntity(user);
                payment.setBookEntity(book);
                payment.setDecimal(BigDecimal.valueOf(bookPrice));
                payment.setCreatedDate(new Date());
                payment.setSuccess(true);
                paymentService.save(payment);

                // Redirect to the book reading page
                return "redirect:/read-book/" + book.getId();
            } else {
                model.addAttribute("error", "You don't have enough points to buy this book.");
            }
        } else {
            model.addAttribute("error", "Error processing your purchase. Please try again.");
        }

        boolean isOwned = paymentService.existsByAccountEntityAndBookEntity(user, book);
        model.addAttribute("book", book);
        model.addAttribute("isOwned", isOwned);
        return "book-view/books-detail";
    }

    @GetMapping("/read-book/{bookId}")
    public String readBook(@PathVariable Long bookId, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        BookEntity book = bookService.getBookById(bookId);
        AccountEntity user = accountService.findByUsername(principal.getName());

        if (book != null && user != null) {
            MembershipEntity membership = user.getMembershipEntity();
            boolean isOwned = paymentService.existsByAccountEntityAndBookEntity(user, book);

            if (membership != null || isOwned) {
                model.addAttribute("book", book);
                return "book-view/books-read";
            } else {
                model.addAttribute("error", "You do not have access to read this book. Please purchase the book first.");
                return "redirect:/books/" + book.getIsbn();
            }
        } else {
            model.addAttribute("error", "Book not found.");
            return "redirect:/error404";
        }
    }
}

