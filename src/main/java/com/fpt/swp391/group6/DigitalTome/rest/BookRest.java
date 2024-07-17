package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.service.AccountService;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
public class BookRest {

    private BookService bookService;
    private ContributionService contributionService;
    private AccountService accountService;

    @Autowired
    public BookRest(BookService bookService, ContributionService contributionService,AccountService accountService) {
        this.accountService = accountService;
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
    public BookEntity getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }


    @PostMapping("/search")
    public AbstractOutput<BookDetailDto> getBooks(@RequestBody SearchPageableRequest request) {
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

    @GetMapping("/statistics")
    public Map<String, Object> getBookStatistics(Principal principal, @RequestParam String period) {
        AccountEntity accountEntity = accountService.findByUsername(principal.getName());
        Long accountId = accountEntity.getId();

        Date startDate = calculateStartDate(period);
        Date endDate = new Date();

        // Lấy danh sách sách mà tài khoản sở hữu từ ContributionEntity
        List<Long> ownedBookIds = contributionService.getBookIdsByAccountId(accountId);

        // Lấy danh sách các thanh toán liên quan đến các sách sở hữu
        List<PaymentEntity> payments = bookService.getPaymentsByBookIdsAndDateRange(ownedBookIds, startDate, endDate);
        BigDecimal totalRevenue = bookService.calculateTotalRevenue(payments);
        int totalBooksSold = bookService.calculateTotalBooksSold(payments);

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        response.put("totalBooksSold", totalBooksSold);
        response.put("startDate", startDate);
        response.put("endDate", endDate);

        // Nhóm thanh toán theo khoảng thời gian đã chọn
        Map<String, Integer> booksSoldDetails = bookService.groupPaymentsByPeriod(payments, period, true);
        Map<String, BigDecimal> revenueDetails = bookService.groupRevenueByPeriod(payments, period);

        response.put("booksSoldDetails", booksSoldDetails);
        response.put("revenueDetails", revenueDetails);

        return response;
    }

    private Date calculateStartDate(String period) {
        Calendar calendar = Calendar.getInstance();
        switch (period) {
            case "7days":
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case "1month":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "1year":
                calendar.add(Calendar.YEAR, -1);
                break;
            case "all":
                calendar.set(Calendar.YEAR, 2000);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        return calendar.getTime();
    }

    @GetMapping("/search/des")
    public ResponseEntity<AbstractOutput<BookDetailDto>> SeekBookByDes(@RequestParam String des){
        AbstractOutput<BookDetailDto> result = new AbstractOutput<>();
        result.setListResults(bookService.getListBookDetailDtoByDes(des));
        if(result.getListResults() == null){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

}
