package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdsController {
    private final AdsService adsService;

    @Autowired
    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping("/advertisement")
    public String ads() {
        return "book-manager/ads";
    }

    @GetMapping("/advertisement/success")
    public String adsSuccess(            @RequestParam("paymentId") String paymentId,
                                         @RequestParam("PayerID") String payerId,
                                         @RequestParam("adsId") Long adsId) {

        return adsService.paymentSuccess(paymentId, payerId, adsId);
    }
}
