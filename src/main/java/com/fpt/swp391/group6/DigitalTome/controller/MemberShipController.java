package com.fpt.swp391.group6.DigitalTome.controller;


import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MembershipEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.service.MembershipService;
import com.fpt.swp391.group6.DigitalTome.service.PaypalService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberShipController {

    private final PaypalService paypalService;
    private final MembershipService membershipService;
    private final UserService userService;


    @GetMapping("/payment/premium")
    public String upgrade() {
        return "payment/premium";
    }

    @PostMapping("/payment/upgrade")
    public RedirectView createPayment(
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description,
            @RequestParam("planType") String planType
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success/upgrade?planType=" + planType;

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

    @GetMapping("/payment/success/upgrade")
    public String paymentSuccessUpgrade(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("planType") String planType,
            Principal principal
    ) {
        PaymentEntity paymentEntity = new PaymentEntity();
        String username = principal.getName();
        AccountEntity accountEntity = userService.findByUsername(username);

        MembershipEntity membershipEntity = accountEntity.getMembershipEntity();
        String currentMembership = membershipEntity != null ? membershipEntity.getName() : "basic";

        if (!canUpgrade(currentMembership, planType)) {

            return "redirect:/payment/error";
        }

        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            List<Transaction> transactions = payment.getTransactions();
            String total = transactions.get(0).getAmount().getTotal();

            membershipService.upgradeUserToMembership(username, planType);

            paymentEntity.setAccountEntity(accountEntity);
            paymentEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            paymentEntity.setSuccess(true);
            paymentEntity.setDecimal(new BigDecimal(total));

            return "payment/paymentSuccess";
        } catch (PayPalRESTException e) {
            log.error("Error occurred during payment: ", e);
        }

        return "redirect:/payment/error";
    }

    private boolean canUpgrade(String currentMembership, String newMembership) {
        if (currentMembership.equals("basic")) {
            return true;
        } else if (currentMembership.equals("standard")) {
            return newMembership.equals("premium");
        } else {
            return false;
        }
    }

}