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

    public void banUser(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(1);
            userRepository.save(user);
        }

    public void unbanUser(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(0);
            userRepository.save(user);
    }

    public void banPublisher(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(1);
            userRepository.save(user);
        }

    public void unbanPublisher(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(0);
        userRepository.save(user);
    }

    public void banEmployee(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(1);
            userRepository.save(user);
    }

    public void unbanEmployee(Long userId) {
        AccountEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setStatus(0);
            userRepository.save(user);
    }
}