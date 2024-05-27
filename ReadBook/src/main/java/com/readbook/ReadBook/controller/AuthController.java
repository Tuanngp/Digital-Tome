package com.readbook.ReadBook.controller;

import com.readbook.ReadBook.dto.UserDto;
import com.readbook.ReadBook.entity.AccountEntity;
import com.readbook.ReadBook.service.EmailService;
import com.readbook.ReadBook.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static com.readbook.ReadBook.utils.UserUtils.generateToken;
import static com.readbook.ReadBook.utils.UserUtils.isTokenExpired;


@Controller
public class AuthController {

    private final EmailService emailService;
    private final HttpSession httpSession;
    private final AuthenticationConfiguration authenticationConfiguration;
    private UserService userService;
    public AuthController(UserService userService, EmailService emailService, OAuth2AuthorizedClientService authorizedClientService, ClientRegistrationRepository clientRegistrationRepository, HttpSession httpSession, AuthenticationConfiguration authenticationConfiguration) {
        this.userService = userService;
        this.emailService = emailService;
        this.httpSession = httpSession;
        this.authenticationConfiguration = authenticationConfiguration;
    }


    @GetMapping("index")
    public String home(){
        return "index";
    }


    @GetMapping("/login")
    public String loginForm() {
        return "shop-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        AccountEntity user = userService.findEmailAndPassword(email, password);
        if(user == null){
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }else if(user != null){

            session.setAttribute("user", user);
            if(user.getRoleEntity().getName().equals("ADMIN")){
                session.setAttribute("user", user);
                return "redirect:/admin";
            }else if(user.getRoleEntity().getName().equals("USER")){
                session.setAttribute("user", user);
                return "redirect:/index";
            }
        }
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

        String otp = generateToken();

        httpSession.setAttribute("otp", otp);
        httpSession.setAttribute("otpCreationTime", LocalDateTime.now()); // LÆ°u thá»i gian táº¡o OTP
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
        if (otpCreationTime == null || isTokenExpired(otpCreationTime)) {
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

        AccountEntity user = userService.findByEmail(email);
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
        return "redirect:/login";
    }

}



