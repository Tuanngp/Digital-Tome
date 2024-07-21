package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentPageDTOResponse;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import com.fpt.swp391.group6.DigitalTome.service.PaypalService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;
    private final PaymentRepository paymentRepository;
    private final UserService userService;

    @GetMapping("/buypoint")
    public String home() {
        return "payment/buypoint";
    }


    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String successUrl = "http://localhost:8080/payment/success";
            String cancelUrl = "http://localhost:8080/payment/cancel";

//            String cancelUrl = "https://digitaltome.azurewebsites.net/payment/cancel";
//            String successUrl = "https://digitaltome.azurewebsites.net/payment/success";


            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    "Paypal",
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            Principal principal
    ) {
        PaymentEntity paymentEntity = new PaymentEntity();
        String username = principal.getName();
        AccountEntity accountEntity = userService.findByUsername(username);

        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            List<Transaction> transactions = payment.getTransactions();
            String total = transactions.get(0).getAmount().getTotal();

            long point = Math.round(Double.parseDouble(total));
            accountEntity.setPoint(accountEntity.getPoint() + point);
            userService.updatePoint(accountEntity);

            paymentEntity.setAccountEntity(accountEntity);
            paymentEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            paymentEntity.setSuccess(true);
            paymentEntity.setDecimal(new BigDecimal(total));

            if (payment.getState().equals("approved")) {
                return "payment/paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
            paymentEntity.setSuccess(false);
        } finally {
            paymentRepository.save(paymentEntity);
        }
        return paymentEntity.isSuccess() ? "payment/paymentSuccess" : "redirect:/payment/error";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "payment/paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "payment/paymentError";
    }

    @GetMapping("/transaction")
    public String transactionHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        AccountEntity accountCurrent = userService.getCurrentLogin();

        if (accountCurrent != null) {
            if (startDate == null) {
                startDate = LocalDate.MIN;
            }
            if (endDate == null) {
                endDate = LocalDate.now();
            }

            PaymentPageDTOResponse response = paypalService.searchPaymentsByAccountIdAndDateRange(accountCurrent.getId(), startDate, endDate, page, size);

            int startIndex = page * size + 1;
            int endIndex = startIndex + size - 1;

            model.addAttribute("transactions", response.getPayments());
            model.addAttribute("totalPages", response.getTotalPages());
            model.addAttribute("currentPage", response.getCurrentPage());
            model.addAttribute("pageSize", size);
            model.addAttribute("startIndex", startIndex);
            model.addAttribute("endIndex", endIndex);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        }
        return "payment/history";
    }
}