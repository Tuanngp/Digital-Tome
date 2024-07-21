package com.fpt.swp391.group6.DigitalTome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("censor/advertisement")
public class AdvertisementController {

    @GetMapping
    public String Advertisement() {
        return "advertisement-validation/advertisementList";
    }

    @GetMapping("/rejection")
    public String rejection() {
        return "advertisement-validation/rejectedAdvertisement";
    }

    @GetMapping("/acception")
    public String acception() {
        return "advertisement-validation/acceptedAdvertisement";
    }
}
