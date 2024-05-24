package com.example.registrationlogin.controller;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.service.EmailService;
import com.example.registrationlogin.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {

    private final EmailService emailService;
    private UserService userService;
    public AuthController(UserService userService, EmailService emailService, OAuth2AuthorizedClientService authorizedClientService, ClientRegistrationRepository clientRegistrationRepository) {
        this.userService = userService;
        this.emailService = emailService;

    }

    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "shop-login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "shop-registration";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "shop-registration";
        }

        User existingEmail = userService.findByEmail(user.getEmail());
        if (existingEmail != null) {
            model.addAttribute("errorMessage", "Email already exists. Please use a different email.");
            return "shop-registration";
        }

        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists. Please use a different username.");
            return "shop-registration";

        }
        userService.saveUser(user);
        return "redirect:/login";
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
    public String resetPass(@RequestParam String token, @RequestParam String password){
        userService.resetPass(token,password);
        return "redirect:/login";
    }
}


