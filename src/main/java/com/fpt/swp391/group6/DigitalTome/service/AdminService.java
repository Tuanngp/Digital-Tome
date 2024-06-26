package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private UserRepository userRepository;

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