package com.fpt.swp391.group6.DigitalTome.controller;


<<<<<<< HEAD
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PublisherEntity;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.service.PublisherService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
=======
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
>>>>>>> origin/khanhduc-workspace
public class RegisterPublisherController {

    private final UserService userService;
    private final PublisherService publisherService;
<<<<<<< HEAD
    private final EmailService emailService;

    @Autowired
    public RegisterPublisherController(UserService userService, PublisherService publisherService, EmailService emailService) {
        this.userService = userService;
        this.publisherService = publisherService;
        this.emailService = emailService;
    }
=======
>>>>>>> origin/khanhduc-workspace

    @GetMapping("/register-publisher")
    public String registerPublisher( Model model) {
        AccountEntity currentUser = userService.getCurrentLogin();
        if (currentUser != null) {
            model.addAttribute("id", currentUser.getId());
            model.addAttribute("email", currentUser.getEmail());
            model.addAttribute("username", currentUser.getUsername());
        }
        return "admin/register-publisher";
    }
<<<<<<< HEAD


    @PostMapping("/submit-register-publisher")
    public String submitRegisterPublisher(PublisherEntity publisher, RedirectAttributes redirectAttributes) {
        // Lấy thông tin người dùng hiện tại
        AccountEntity accountCurrent = userService.getCurrentLogin();

        if (accountCurrent != null) {
            // Kiểm tra xem người dùng đã có yêu cầu chưa được duyệt hay không
            if (publisherService.existsByUserId(accountCurrent.getId())) {
                redirectAttributes.addFlashAttribute("error", "You have already submitted a registration request. Please wait for admin approval.");
                return "redirect:/register-publisher";
            }

            // Lưu thông tin đăng ký
            publisher.setUser(accountCurrent);
            publisherService.savePublisherRequest(publisher);

            // Gửi email xác nhận
            String subject = "Publisher Registration Confirmation";
            String htmlContent = "Dear " + accountCurrent.getUsername() + ",<br/>Your request to become a publisher has been received. We will review your request shortly.";
            try {
                emailService.sendEmail(subject, htmlContent, List.of(accountCurrent.getEmail()));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        redirectAttributes.addFlashAttribute("success", "You have successfully registered, please give us your feedback");
        return "redirect:/register-publisher";
    }
}
=======
    @PostMapping("/submit-register-publisher")
    public String submitRegisterPublisher(@ModelAttribute RegisterPublisherDTO registerPublisherDTO, RedirectAttributes redirectAttributes) {
        return publisherService.registerPublisher(registerPublisherDTO,redirectAttributes);
    }
}


>>>>>>> origin/khanhduc-workspace
