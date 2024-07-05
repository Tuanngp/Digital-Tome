package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentPageDTOResponse;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.service.PaypalService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaypalService paypalService;

    @GetMapping("/transactions")
    public PaymentPageDTOResponse searchTransactions(
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        AccountEntity accountCurrent = userService.getCurrentLogin();

        if (accountCurrent != null) {
            return paypalService.searchPaymentsByAccountIdAndDateRange(accountCurrent.getId(), startDate, endDate, page, size);
        }
        return new PaymentPageDTOResponse();
    }

    @GetMapping("/transactions/all")
    public PaymentPageDTOResponse getAllTransactions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        AccountEntity accountCurrent = userService.getCurrentLogin();

        if (accountCurrent != null) {
            if (startDate == null) {
                startDate = LocalDate.MIN;
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }

            return paypalService.searchPaymentsByAccountIdAndDateRange(accountCurrent.getId(), startDate, endDate, page, size);
        }

        return new PaymentPageDTOResponse();
    }
}
