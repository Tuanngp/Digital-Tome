package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.ProfileDto;
import com.fpt.swp391.group6.DigitalTome.service.ProfileService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Controller
public class ProfileController {


    private ProfileService profileService;
    private UserService userService;


    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;

    }

    @GetMapping("/profile")
    public String formProfile(Model model, Principal principal) {
        String userName = principal.getName();
        model.addAttribute("profileDto", profileService.findViewProfile(userName));
        return "my-profile";
    }

    @PostMapping("/profile") // Sau khi UPDATE Profile
    public String profile(@ModelAttribute("profileDto") ProfileDto profileDto) {
        profileService.updateProfile(profileDto);
        return "redirect:/profile";
    }


    @PostMapping("/profileUrl")
    public String updateAvatar(@RequestParam("file") MultipartFile file, Principal principal) {
        if (!file.isEmpty()) {
            String fileName = "/user/images/user" + file.getOriginalFilename();

            Path path = Paths.get("src/main/resources/static/" + fileName);
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
    public String changePassword() {
        return "change-password";
    }

    @PostMapping("/savePassword")
    public String savePassWord(@RequestParam("old_password") String oldPassword,
                               @RequestParam("new_password") String newPassword,
                               @RequestParam("confirm_password") String confirmPassword,
                               Principal principal,
                               Model model
    ) {
        String username = principal.getName();

        if (!profileService.confirmPassword(username, oldPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }

        if (newPassword.equals(oldPassword)) {
            model.addAttribute("error", "Old and new passwords cannot be the same");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password does not match the confirm password");
            return "change-password";
        }


        profileService.newPassword(username, newPassword);
        return "redirect:/profile";
    }

}
