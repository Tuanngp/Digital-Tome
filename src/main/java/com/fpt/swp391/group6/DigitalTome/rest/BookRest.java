package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchPageableRequest;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import com.fpt.swp391.group6.DigitalTome.rest.output.AbstractOutput;
import com.fpt.swp391.group6.DigitalTome.rest.output.BookOutput;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookRest {

    private BookService bookService;
    private ContributionService contributionService;

    @Autowired
    public BookRest(BookService bookService, ContributionService contributionService) {
        this.bookService = bookService;
        this.contributionService = contributionService;
    }

    @PutMapping("/acception")
    public ResponseEntity<String> acceptBook(@RequestParam String isbn) {
        bookService.updateStatusByIsbn(2,isbn);
        contributionService.updateModifiedDateByBookEntity_ISBN(isbn);
        return ResponseEntity.ok("Book accepted successfully!");
    }

    @PutMapping("/rejection")
    public ResponseEntity<String> rejectBook(@RequestParam String isbn) {
        bookService.updateStatusByIsbn(3,isbn);
        contributionService.updateModifiedDateByBookEntity_ISBN(isbn);
        return ResponseEntity.ok("Book rejection successfully!");
    }

    @GetMapping("/detail")
    public BookOutput getDetail(@RequestParam String isbn) {
        BookOutput bookOutput = new BookOutput();
        bookOutput.setBookDetailDto(bookService.findByIsbnIncludeCategoriesAndAuthors(isbn));
        return bookOutput;
    }

    @GetMapping("/book-list-detail")
    public AbstractOutput<BookDetailDto> getBookListDetail(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int limit) {
        AbstractOutput<BookDetailDto> list = new AbstractOutput<>();
        list.setListResults(this.bookService.findByStatus(2, PageRequest.of(page-1,limit)));
        list.setCurrentPage(page);
        list.setTotalPages((int)Math.ceil((double) this.bookService.countBookByStatus(2)/(double) limit));
        return list;
    }

    @GetMapping("/{id}")
    public BookEntity getBookById(@PathVariable int id) {
        BookEntity bookEntity = bookService.getBookById(id);
        return bookEntity;
    }


    @PostMapping("/search")
    public AbstractOutput<BookDetailDto> getBookByTitle(@RequestBody SearchPageableRequest request) {
        AbstractOutput<BookDetailDto> result = new AbstractOutput<>();
        Sort sort = Sort.by((request.getSortDirValue().equals("asc"))?Sort.Direction.ASC:Sort.Direction.DESC,request.getSortByValue() );
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(),sort);
        SearchRequest searchRequest = new SearchRequest(
                request.getKeyword(),
                request.getCategories(),
                request.getYears(),
                request.getMinPoint(),
                request.getMaxPoint()
        );

        Page<BookDetailDto> bookDetailDtoPage = this.bookService.search(searchRequest, pageable);

        result.setListResults(bookDetailDtoPage.getContent());
        result.setCurrentPage(pageable.getPageNumber()+1);
        result.setTotalPages(bookDetailDtoPage.getTotalPages());

        return result;
    }


}
