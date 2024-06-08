package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.RegisterDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static com.fpt.swp391.group6.DigitalTome.utils.UserUtils.generateToken;
import static com.fpt.swp391.group6.DigitalTome.utils.UserUtils.isTokenExpired;

@Controller
public class AuthController {

    private final EmailService emailService;
    private final HttpSession httpSession;
    private UserService userService;

    public AuthController(UserService userService, EmailService emailService, HttpSession httpSession) {
        this.userService = userService;
        this.emailService = emailService;
        this.httpSession = httpSession;
    }


    @GetMapping(value = {"/","home", "index"})
    public String defaultHome() { return "landing-page/index"; }

    @GetMapping("/login")
    public String loginForm() {
        return "authentication/shop-login";
    }


    @PostMapping("/login")
    public String checkLogin(@RequestParam("username") String email, @RequestParam("password") String password,
                             Model model, RedirectAttributes redirectAttributes) {
        AccountEntity user = userService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Email does not exist.");
            return "redirect:/login";
        }
        if (user.getStatus() == 1) {
            redirectAttributes.addFlashAttribute("error", "Account is banned");
            return "redirect:/login";
        }
        if (!userService.checkLogin(email, password)) {
            redirectAttributes.addFlashAttribute("error", "Invalid password.");
            return "redirect:/login";
        }

        return "redirect:/";
    }


    @GetMapping("register")
    public String showRegistrationForm(Model model){
        RegisterDto user = new RegisterDto();
        model.addAttribute("user", user)    ;
        return "authentication/shop-registration";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterDto userDto,
                               BindingResult result,
                               Model model, RedirectAttributes redirectAttributes) {

        // Validation
        if (result.hasErrors())
            return "authentication/shop-registration";

        if (userService.existsByEmail(userDto.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
            return "authentication/shop-registration";
        }
        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists. Please use a different username.");
            return "authentication/shop-registration";
        }

        String otp = generateToken();
        httpSession.setAttribute("otp", otp);
        httpSession.setAttribute("otpCreationTime", LocalDateTime.now());
        httpSession.setAttribute("tempUserDto", userDto);

        try {
            emailService.sendEmail("Code OTP", "Your OTP code is: " + otp, List.of(userDto.getEmail()));

        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send OTP. Please try again.");
            e.printStackTrace();
            return "authentication/shop-registration";
        }
        model.addAttribute("user", userDto);
        return "redirect:/otp";
    }

    @GetMapping("/otp")
    public String otp(){
        return "authentication/otp";
    }

    @PostMapping("/register/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            HttpSession session,
                            Model model
                            ) {

        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpCreationTime = (LocalDateTime) session.getAttribute("otpCreationTime");
        RegisterDto userDto = (RegisterDto) session.getAttribute("tempUserDto");

        if (otpCreationTime == null || isTokenExpired(otpCreationTime)) {
            model.addAttribute("error", "OTP has expired");
            return "authentication/otp";
        }

        if (sessionOtp != null && sessionOtp.equals(otp) && userDto != null) {

            userService.saveUser(userDto);
            session.removeAttribute("otp");
            session.removeAttribute("otpCreationTime");
            session.removeAttribute("tempUserDto");
            return "redirect:/login";

        } else {
            model.addAttribute("error", "Invalid OTP! Please try again.");
            return "authentication/otp";

        }
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "authentication/forgotPassword";
    }

    @PostMapping("/sendEmail")
    public String sendResetPasswordEmail(@RequestParam("email") String email, Model model){

        AccountEntity user = userService.findByEmail(email);
        if(user == null){
            model.addAttribute("error", "Email does not exist");
            return "authentication/forgotPassword";
        }

        String otp = userService.forgotPass(email);
        try {
            emailService.sendEmail("Reset PassWord","Your OTP code is: " + otp, List.of(user.getEmail()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "authentication/newPassword";
    }



    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            String result = userService.resetPass(token, password);

            if ("Token expired.".equals(result) || "Invalid token".equals(result)) {
                model.addAttribute("errorMessage", result);
                return "authentication/newPassword";
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "authentication/newPassword";
        }
        return "redirect:/login";
    }
}



