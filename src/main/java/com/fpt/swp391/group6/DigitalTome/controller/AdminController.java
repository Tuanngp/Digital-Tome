package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.PublisherDTO;
import com.fpt.swp391.group6.DigitalTome.dto.UserBanDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PublisherService publisherService;
    private final PaymentService paymentService;


    @GetMapping("/admin")
    public String homeAdmin(Model model) {
        BigDecimal totalAmount = paymentService.calculateTotalAmount();
        model.addAttribute("totalAmount", totalAmount);

        BigDecimal male = userService.getMalePercentage();
        BigDecimal female = userService.getFemalePercentage();
        BigDecimal other = userService.getOtherPercentage();

        model.addAttribute("malePercentage", male);
        model.addAttribute("femalePercentage", female);
        model.addAttribute("otherPercentage", other);

        return "admin/index_admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<AccountEntity> accounts = userService.fetchAllAccount();

        long userCount = accounts.stream()
                .filter(t -> "ROLE_USER".equals(t.getRoleEntity().getName()))
                .count();
        long publisherCount = accounts.stream()
                .filter(t -> "ROLE_PUBLISHER".equals(t.getRoleEntity().getName()))
                .count();

        long censorCount = accounts.stream()
                .filter(t -> "ROLE_CENSOR".equals(t.getRoleEntity().getName()))
                .count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("publisherCount", publisherCount);
        model.addAttribute("censorCount", censorCount);
        model.addAttribute("page", "dashboard");
        return "admin/manager";
    }

    @GetMapping("/user-manager")
    public String userManager(Model model) {
        List<AccountEntity> user = userRepository.findByRoleName("ROLE_USER");
        model.addAttribute("users", user);
        model.addAttribute("page", "user-manager");
        return "admin/manager";
    }

    @GetMapping("/publisher-manager")
    public String publisherManager(Model model) {
        List<AccountEntity> publisher = userRepository.findByRoleName("ROLE_PUBLISHER");
        model.addAttribute("users", publisher);
        model.addAttribute("page", "publisher-manager");
        return "admin/manager";
    }


    @GetMapping("/censor-manager")
    public String censorManager(Model model){
        List<AccountEntity> censor = userRepository.findByRoleName("ROLE_CENSOR");
        model.addAttribute("users", censor);
        model.addAttribute("page", "censor-manager");
        return "admin/manager";
    }

    @GetMapping("/register-publisher-details")
    public String showRegisterPublisherDetails(Model model) {
        List<PublisherDTO> publisherList = publisherService.getAllPublisherDetails();
        model.addAttribute("publisher", publisherList);
        model.addAttribute("page", "register-publisher");
        return "admin/manager";
    }

    @GetMapping("/admin-chart")
    public String showAdminStatisticsPage() {
        return "admin/admin-chart";
    }


    @PostMapping("/user/ban")
    @ResponseBody
    public String banUser(@RequestBody UserBanDto userDto) {
        return processUserBanUnban(userDto, true);
    }

    @PostMapping("/user/unban")
    @ResponseBody
    public String unbanUser(@RequestBody UserBanDto userDto) {
        return processUserBanUnban(userDto, false);
    }

    @PostMapping("/publisher/ban")
    @ResponseBody
    public String banPublisher(@RequestBody UserBanDto userDto) {
        return processPublisherBanUnban(userDto, true);
    }

    @PostMapping("/publisher/unban")
    @ResponseBody
    public String unbanPublisher(@RequestBody UserBanDto userDto) {
        return processPublisherBanUnban(userDto, false);
    }

    @PostMapping("/censor/ban")
    @ResponseBody
    public String banEmployee(@RequestBody UserBanDto userDto) {
        return processEmployeeBanUnban(userDto, true);
    }

    @PostMapping("/censor/unban")
    @ResponseBody
    public String unbanEmployee(@RequestBody UserBanDto userDto) {
        return processEmployeeBanUnban(userDto, false);
    }


    private String processUserBanUnban(UserBanDto userDto, boolean ban) {
        if (ban) {
            adminService.banUser(userDto.getId());
        } else {
            adminService.unbanUser(userDto.getId());
        }
        return sendNotification(userDto, ban ? "Account Banned Notification" : "Account Unbanned Notification");
    }

    private String processPublisherBanUnban(UserBanDto userDto, boolean ban) {
        if (ban) {
            adminService.banPublisher(userDto.getId());
        } else {
            adminService.unbanPublisher(userDto.getId());
        }
        return sendNotification(userDto, ban ? "Account Banned Notification" : "Account Unbanned Notification");
    }

    private String processEmployeeBanUnban(UserBanDto userDto, boolean ban) {
        if (ban) {
            adminService.banEmployee(userDto.getId());
        } else {
            adminService.unbanEmployee(userDto.getId());
        }
        return sendNotification(userDto, ban ? "Account Banned Notification" : "Account Unbanned Notification");
    }

    private String sendNotification(UserBanDto userDto, String subject) {
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return subject + " successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }
}