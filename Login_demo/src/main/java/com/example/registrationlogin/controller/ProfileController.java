package com.example.registrationlogin.controller;

import com.example.registrationlogin.dto.ProfileDto;
import com.example.registrationlogin.service.ProfileService;
import com.example.registrationlogin.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class ProfileController {

    private final PasswordEncoder passwordEncoder;
    private ProfileService profileService;
    private UserService userService;

    public ProfileController(ProfileService profileService, UserService userService, PasswordEncoder passwordEncoder) {
        this.profileService = profileService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String formProfile(Model model, Principal principal) {
        String userName = principal.getName();

        model.addAttribute("profileDto", profileService.findViewProfile(userName));
        model.addAttribute("view", profileService.findViewProfile(userName));
//        session.setAttribute("check",userService.existsByUsername(userName));
        return "my-profile";
    }

    @PostMapping("/profile")
    public String profile(@ModelAttribute("profileDto") ProfileDto profileDto) {
        profileService.updateProfile(profileDto);
        return "redirect:/profile";
    }

    @PostMapping("/profileUrl")
    public String updateAvatar(@RequestParam("file") MultipartFile file, Principal principal) {
        if (!file.isEmpty()) {
            String fileName = "/images/user/" + file.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static" + fileName);
            try (OutputStream os = Files.newOutputStream(path)) {
                os.write(file.getBytes());
                userService.updateImage(fileName, principal.getName());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save file.");
            }
        }
        return "redirect:/profile";
    }

    @GetMapping("changePassword")
    public String changePassword(){
        return "change-password";
    }

    @PostMapping("/savePassword")
    public String savePassWord(@RequestParam("old_password") String oldPassword,
                               @RequestParam("new_password") String newPassword,
                               @RequestParam("confirm_password") String confirmPassword,
                               Principal principal,
                               Model model,
                               RedirectAttributes redirectAttributes
    ) {
        if (!profileService.confirmPassword(principal.getName(), oldPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password is incorrect");
            return "change-password";
        }

        profileService.newPassword(principal.getName(), newPassword);
        redirectAttributes.addFlashAttribute("success", "Password updated successfully");
        return "redirect:/profile";
    }

}

