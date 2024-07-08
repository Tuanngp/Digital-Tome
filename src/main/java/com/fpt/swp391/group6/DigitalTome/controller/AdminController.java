package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.UserBanDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import com.fpt.swp391.group6.DigitalTome.service.AdminService;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserService userService;
//    private final PublisherService publisherService;

<<<<<<< HEAD
<<<<<<< HEAD
    public AdminController(AdminService adminService, EmailService emailService, UserRepository userRepository, UserService userService, PublisherService publisherService) {
=======
    public AdminController(AdminService adminService, EmailService emailService, UserRepository userRepository, UserService userService) {
>>>>>>> 728ce2091d5a52ed77fa453748e001245b19c9ed
        this.adminService = adminService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userService = userService;
<<<<<<< HEAD
        this.publisherService = publisherService;
=======
//        this.publisherService = publisherService;

>>>>>>> 728ce2091d5a52ed77fa453748e001245b19c9ed
    }
=======
>>>>>>> origin/khanhduc-workspace

    @GetMapping("/admin")
    public String homeAdmin() {
        return "admin/index_admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
<<<<<<< HEAD
        model.addAttribute("page", "dashboard");
        return "admin/manager";
    }

    @GetMapping("/manager")
    public String manager(Model model) {
=======
        List<AccountEntity> accounts = userService.fetchAllAccount();

        long userCount = accounts.stream()
                .filter(t -> "ROLE_USER".equals(t.getRoleEntity().getName()))
                .count();
        long publisherCount = accounts.stream()
                .filter(t -> "ROLE_PUBLISHER".equals(t.getRoleEntity().getName()))
                .count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("publisherCount", publisherCount);
>>>>>>> origin/khanhduc-workspace
        model.addAttribute("page", "dashboard");
        return "admin/manager";
    }


    @GetMapping("/user-manager")
    public String userManager(Model model) {
        List<AccountEntity> users = userRepository.findByRoleName("ROLE_USER");
        model.addAttribute("users", users);
        model.addAttribute("page", "user-manager");
        return "admin/manager";
    }

    @GetMapping("/publisher-manager")
    public String publisherManager(Model model) {
        List<AccountEntity> publishers = userRepository.findByRoleName("ROLE_PUBLISHER");
        model.addAttribute("users", publishers);
        model.addAttribute("page", "publisher-manager");
        return "admin/manager";
    }

    @GetMapping("/employee-manager")
    public String employeeManager(Model model) {
        List<AccountEntity> employees = userRepository.findByRoleName("ROLE_EMPLOYEE");
        model.addAttribute("users", employees);
        model.addAttribute("page", "employee-manager");
        return "admin/manager";
    }

    @PostMapping("/user/ban")
    @ResponseBody
    public String banUser(@RequestBody UserBanDto userDto) {
        adminService.banUser(userDto.getId());
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Banned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "User banned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }

    @PostMapping("/user/unban")
    @ResponseBody
    public String unbanUser(@RequestBody UserBanDto userDto) {
        adminService.unbanUser(userDto.getId());
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Unbanned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "User unbanned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }

    @PostMapping("/publisher/ban")
    @ResponseBody
    public String banPublisher(@RequestBody UserBanDto userDto) {
        adminService.banPublisher(userDto.getId());
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Banned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "Publisher banned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }

    @PostMapping("/publisher/unban")
    @ResponseBody
    public String unbanPublisher(@RequestBody UserBanDto userDto) {
        adminService.unbanPublisher(userDto.getId());

        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Unbanned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "Publisher unbanned successfully";
        } catch (Exception e) {
            return "Email sending failed.";
        }
    }

    @PostMapping("/employee/ban")
    @ResponseBody
    public String banEmployee(@RequestBody UserBanDto userDto) {
        adminService.banEmployee(userDto.getId());
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Banned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "Employee banned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }

    @PostMapping("/employee/unban")
    @ResponseBody
    public String unbanEmployee(@RequestBody UserBanDto userDto) {
        adminService.unbanEmployee(userDto.getId());
        String userEmail = userService.getEmailById(userDto.getId());
        try {
            String subject = "Account Unbanned Notification";
            emailService.sendEmail(subject, userDto.getReason(), Collections.singletonList(userEmail));
            return "Employee unbanned successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Email sending failed.";
        }
    }

//    @GetMapping("/register-publisher-details")
//    public String showRegisterPublisherDetails(Model model) {
//        List<PublisherDTO> publisherList = publisherService.getAllPublisherDetails();
//        model.addAttribute("publisher", publisherList);
//        model.addAttribute("page", "register-publisher");
//        return "admin/manager";
//    }
}