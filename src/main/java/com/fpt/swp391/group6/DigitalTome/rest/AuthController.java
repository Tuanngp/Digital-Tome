package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.RegisterDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static com.fpt.swp391.group6.DigitalTome.utils.UserUtils.generateToken;
import static com.fpt.swp391.group6.DigitalTome.utils.UserUtils.isTokenExpired;

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


    // Trang home
    @GetMapping(value = {"/", "/index"})
    public String home(){
        return "landing-page/index";
    }


    // Trang Login
    @GetMapping("/login")
    public String loginForm() {
        return "authentication/shop-login";
    }


    // Xử lý login
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
            return "authentication/shop-login";
        }

     // Trang đăng kí tài khoản
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        RegisterDto user = new RegisterDto();
        model.addAttribute("user", user)    ;
        return "authentication/shop-registration";
    }


    // Trang xử lý validation, nếu error trả lại trang đăng kí, không lỗi --> xác thực OTP
    @PostMapping("/authenOtp")
    public String saveUser(@Valid @ModelAttribute("user") RegisterDto userDto,
                           BindingResult result,
                           Model model, RedirectAttributes redirectAttributes) {

        // Validation
        if (result.hasErrors()) {
            return "authentication/shop-registration";
        }

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
            emailService.sendHtmlEmail(userDto.getEmail(), otp);
        } catch (MessagingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send OTP. Please try again.");
            e.printStackTrace();
            return "authentication/shop-registration";
        }
        model.addAttribute("user", userDto);
        return "redirect:/otp";
    }

    // Sau khi hoàn thành những thông tin đăng kí --> xác thực OTP đăng kí tài khoản
    @GetMapping("/otp")
    public String otp(){
        return "authentication/otp";
    }



    // Sau khi submit, kiểm tra mã đăng kí có hợp lệ hay không ?
    @PostMapping("/register/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
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


    // Khi click Forgotpassword thì hiện thị form này
    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "authentication/forgotPassword";
    }


    // Sau khi forgotpassword thì kiểm tra email đó có tồn tại không
    // Nếu nhập email đúng --> gửi mã otp và chuyển tới trang newPassword để nhập mật khẩu mới
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("email") String email, Model model){

        AccountEntity user = userService.findByEmail(email);
        if(user == null){
            model.addAttribute("error", "Email does not exist");
            return "authentication/forgotPassword";
        }else{
            String token = userService.forgotPass(email);
            try {
                emailService.sendHtmlEmail(email, token);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return "authentication/newPassword";
    }


    // Sau khi chuyển tới newPassword, khi submit thì chuyển tới đây để xử lý Validation nếu có xảy ra
    @PostMapping("/reset-password")
    public String resetPass(@RequestParam String token, @RequestParam String password, Model model) {
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


    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<RegisterDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

}



