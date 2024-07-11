package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.service.AccountService;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookRest {
    @Autowired
    BookService bookService;

    @Autowired
    AccountService accountService;

    @Autowired
    ContributionService contributionService;

    @GetMapping
    public ResponseEntity<List<BookEntity>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @GetMapping("/{id}")
    public BookEntity getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
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
}
