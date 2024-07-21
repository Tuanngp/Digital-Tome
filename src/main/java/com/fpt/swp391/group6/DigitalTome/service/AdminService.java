package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void updateUserStatus(Long userId, int status) {
        AccountEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
    }

    public void banUser(Long userId) {
        updateUserStatus(userId, 1);
    }

    public void unbanUser(Long userId) {
        updateUserStatus(userId, 0);
    }

    public void banPublisher(Long userId) {
        banUser(userId);
    }

    public void unbanPublisher(Long userId) {
        unbanUser(userId);
    }

    public void banEmployee(Long userId) {
        banUser(userId);
    }

    public void unbanEmployee(Long userId) {
        unbanUser(userId);
    }
}
