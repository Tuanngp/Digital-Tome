package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import com.fpt.swp391.group6.DigitalTome.service.AccountService;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.ContributionService;
import com.fpt.swp391.group6.DigitalTome.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRest {
    
    private final PaymentRepository paymentRepository;

    @GetMapping("/statistics")
    public ResponseEntity<?> getBookStatistics(@RequestParam String period) {
        Map<String, Object> statistics = new HashMap<>();

        // Initialize variables
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        // Determine the start date based on the period
        switch (period) {
            case "7days":
                startDate = endDate.minusDays(7);
                break;
            case "1month":
                startDate = endDate.minusMonths(1);
                break;
            case "1year":
                startDate = endDate.minusYears(1);
                break;
            case "all":
            default:
                startDate = LocalDateTime.of(2015, 1, 1, 0, 0);
                break;
        }

        // Fetch data from the repository
        List<PaymentEntity> payments = paymentRepository.findAllByCreatedDateBetween(startDate, endDate);

        // Process data to generate statistics
        Map<String, Integer> booksSoldDetails = new LinkedHashMap<>();
        Map<String, BigDecimal> revenueDetails = new LinkedHashMap<>();

        for (PaymentEntity payment : payments) {
            LocalDateTime createdDate = payment.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String date = createdDate.toLocalDate().toString();
            booksSoldDetails.put(date, booksSoldDetails.getOrDefault(date, 0) + 1);
            revenueDetails.put(date, revenueDetails.getOrDefault(date, BigDecimal.ZERO).add(payment.getDecimal()));
        }

        statistics.put("booksSoldDetails", booksSoldDetails);
        statistics.put("revenueDetails", revenueDetails);

        return ResponseEntity.ok(statistics);
    }
}
