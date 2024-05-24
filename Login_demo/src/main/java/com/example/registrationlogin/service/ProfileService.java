package com.example.registrationlogin.service;

import com.example.registrationlogin.dto.ProfileDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Service
public class ProfileService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileDto findViewProfile(String username) {
        return userRepository.findByUser(username);
    }

    public void updateProfile(ProfileDto profileDto) {
        Optional<User> userOptional = userRepository.findById(profileDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFullName(profileDto.getFullName());
            user.setPhone(profileDto.getPhone());
            user.setAddress(profileDto.getAddress());
            user.setAge(profileDto.getAge());
            userRepository.save(user);
        }
    }

    public boolean confirmPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        String encodedPassword = userRepository.findPasswordByUsername(user.getUsername());
        return passwordEncoder.matches(password, encodedPassword);
    }

    public void newPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }
}
