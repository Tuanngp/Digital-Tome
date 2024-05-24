package com.example.registrationlogin.controller;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.service.EmailService;
import com.example.registrationlogin.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.time.LocalDateTime;
import java.util.List;

import static com.example.registrationlogin.service.UserService.generateToken;
import static com.example.registrationlogin.service.UserService.isTokenExpired;

@Controller
public class AuthController {

    private final EmailService emailService;
    private final HttpSession httpSession;
    private UserService userService;
    public AuthController(UserService userService, EmailService emailService, OAuth2AuthorizedClientService authorizedClientService, ClientRegistrationRepository clientRegistrationRepository, HttpSession httpSession) {
        this.userService = userService;
        this.emailService = emailService;
        this.httpSession = httpSession;
    }


    @GetMapping("index")
    public String home(){
        return "index";
    }


    @GetMapping("/login")
    public String loginForm() {
        return "shop-login";
    }



    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user)    ;
        return "shop-registration";
    }


    @PostMapping("/authenOtp")
    public String saveUser(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result,
                           Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "shop-registration";
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
            return "shop-registration";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists. Please use a different username.");
            return "shop-registration";
        }

        String otp = UserService.generateToken();

        httpSession.setAttribute("otp", otp);
        httpSession.setAttribute("otpCreationTime", LocalDateTime.now()); // Lưu thời gian tạo OTP
        httpSession.setAttribute("tempUserDto", userDto);

        try {
            emailService.sendHtmlEmail(userDto.getEmail(), otp);
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send OTP. Please try again.");
            e.printStackTrace();
            return "shop-registration";
        }
        model.addAttribute("user", userDto);
        return "redirect:/otp";
    }


    @GetMapping("/otp")
    public String otp(){
        return "otp";
    }

    @PostMapping("/register/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpCreationTime = (LocalDateTime) session.getAttribute("otpCreationTime");
        UserDto userDto = (UserDto) session.getAttribute("tempUserDto");

        // Kiểm tra thời gian tạo OTP
        if (otpCreationTime == null || UserService.isTokenExpired(otpCreationTime)) {
            model.addAttribute("error", "OTP has expired");
            return "otp";
        }


        if (sessionOtp != null && sessionOtp.equals(otp) && userDto != null) {
            userService.saveUser(userDto);
            session.removeAttribute("otp");
            session.removeAttribute("otpCreationTime");
            session.removeAttribute("tempUserDto");
            return "redirect:/login";

        } else {
            model.addAttribute("error", "Invalid OTP! Please try again.");
            return "otp";
        }
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }


    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("email") String email, Model model){

        User user = userService.findByEmail(email);
        if(user == null){
            model.addAttribute("error", "Email does not exist");
            return "forgotPassword";
        }else{
            String token = userService.forgotPass(email);
            try {
                emailService.sendHtmlEmail(email, token);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return "newPassword";
    }


    @PostMapping("/reset-password")
    public String resetPass(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            String result = userService.resetPass(token, password);
            if ("Token expired.".equals(result) || "Invalid token".equals(result)) {
                model.addAttribute("errorMessage", result);
                return "newPassword";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "newPassword";
        }
//        model.addAttribute("successMessage", "Password has been updated successfully.");
        return "redirect:/login";
    }

}


