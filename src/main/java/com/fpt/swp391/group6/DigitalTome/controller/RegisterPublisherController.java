package com.fpt.swp391.group6.DigitalTome.controller;


import com.fpt.swp391.group6.DigitalTome.dto.RegisterPublisherDTO;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.service.PublisherService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegisterPublisherController {

    private final UserService userService;
    private final PublisherService publisherService;

    @GetMapping("/register-publisher")
    public String registerPublisher(Model model) {
        AccountEntity currentUser = userService.getCurrentLogin();
        if (currentUser != null) {
            model.addAttribute("id", currentUser.getId());
            model.addAttribute("email", currentUser.getEmail());
            model.addAttribute("username", currentUser.getUsername());
        }
        return "admin/register-publisher";
    }

    @PostMapping("/submit-register-publisher")
    public String submitRegisterPublisher(@ModelAttribute RegisterPublisherDTO registerPublisherDTO, RedirectAttributes redirectAttributes) {
        return publisherService.registerPublisher(registerPublisherDTO, redirectAttributes);
    }
}