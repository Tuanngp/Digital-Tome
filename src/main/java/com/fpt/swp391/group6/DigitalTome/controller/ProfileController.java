package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.UserDto;
import com.fpt.swp391.group6.DigitalTome.service.ProfileService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

import static com.fpt.swp391.group6.DigitalTome.service.UserService.DEFAULT_AVATAR_URL;

@Controller
public class ProfileController {
<<<<<<< HEAD

=======
    public static final String DEFAULT_AVATAR_URL = "/user/images/profile1.jpg";
>>>>>>> 8d11a83 (update gender)
    private final ProfileService profileService;
    private final UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String formProfile(Model model, Principal principal) {
        String userName = principal.getName();
        UserDto userProfile = profileService.findViewProfile(userName);
        model.addAttribute("profileDto", userProfile);
        return "account/my-profile";
    }

    @PostMapping("/profile")
    public String profile(@Validated @ModelAttribute("profileDto") UserDto profileDto, BindingResult result) {
        if (result.hasErrors()) {
            return "account/my-profile";
        }

        String cleanedFullName = profileDto.getFullName().replaceAll("\\s+", " ").trim();
        profileDto.setFullName(cleanedFullName);

        String cleanedPhone = profileDto.getPhone().replaceAll("\\D+", "").trim();
        profileDto.setPhone(cleanedPhone);

        profileService.updateProfile(profileDto);
        return "redirect:/profile";
    }

    @PostMapping("/profileUrl")
    public String updateAvatar(@RequestParam("file") MultipartFile file,
                               @RequestParam("action") String action,
                               Principal principal) {
        try {
            if ("save".equals(action)) {
                if (!file.isEmpty()) {
                    String imageUrl = userService.uploadImage(file);
                    userService.updateImage(imageUrl, principal.getName());
                }
            } else if ("remove".equals(action)) {
                String imageUrl = userService.getImage(principal.getName());
                userService.destroyImage(imageUrl);

                userService.updateImage(DEFAULT_AVATAR_URL, principal.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile";
    }

    @GetMapping("changePassword")
    public String changePassword() {
        return "authentication/change-password";
    }

    @PostMapping("/savePassword")
    public String savePassWord(@RequestParam("old_password") String oldPassword,
                               @RequestParam("new_password") String newPassword,
                               @RequestParam("confirm_password") String confirmPassword,
                               Principal principal,
                               Model model) {
        String username = principal.getName();

        if (!profileService.confirmPassword(username, oldPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "authentication/change-password";
        }

        if (newPassword.equals(oldPassword)) {
            model.addAttribute("error", "Old and new passwords cannot be the same");
            return "authentication/change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password does not match the confirm password");
            return "authentication/change-password";
        }

        profileService.newPassword(username, newPassword);
        return "redirect:/profile";
    }
}
