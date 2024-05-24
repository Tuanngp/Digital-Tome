package com.example.registrationlogin.controller;

import com.example.registrationlogin.dto.ProfileDto;
import com.example.registrationlogin.service.ProfileService;
import com.example.registrationlogin.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ProfileController {
    private ProfileService profileService;
    private UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/profile")
            public String formProfile(Model model , Principal principal, HttpSession session){
        String userName = principal.getName();

        model.addAttribute("profileDto",profileService.findViewProfile(userName));
        model.addAttribute("view",profileService.findViewProfile(userName));
        session.setAttribute("check",userService.existsByUsername(userName));
        return "my-profile";
    }

    @PostMapping("/profile")
    public String  profile(@ModelAttribute("profileDto") ProfileDto profileDto){
        profileService.updateProfile(profileDto);
        return "redirect:/profile";
    }

    @GetMapping("/header")
    public String header(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
            } else {
                model.addAttribute("username", principal.toString());
            }
            return "header";
        }
        throw new UsernameNotFoundException("User is not authenticated");
    }
}
