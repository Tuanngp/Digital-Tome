package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.UserDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.UserMapper;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.fpt.swp391.group6.DigitalTome.utils.DateUtils.*;

import java.sql.Date;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;


    public UserDto findViewProfile(String username) {
        return userRepository.findByUser(username);
    }

    public void updateProfile(UserDto profileDto) {
        Optional<AccountEntity> userOptional = userRepository.findById(profileDto.getId());
        if (userOptional.isPresent()) {
            AccountEntity user = userOptional.get();
            user.setFullname(profileDto.getFullName());
            user.setPhone(profileDto.getPhone());
            user.setAddress(profileDto.getAddress());
            user.setDescription(profileDto.getDescription());
            user.setPoint(profileDto.getPoint());
//            java.sql.Date date = convertUtilDateToSqlDate(profileDto.getDateOfBirth());
//            user.setDateOfBirth(date);
            userRepository.save(user);
        }
    }

    public boolean confirmPassword(String username, String password) {
        AccountEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        String encodedPassword = userRepository.findPasswordByUsername(user.getUsername());
        return passwordEncoder.matches(password, encodedPassword);
    }

    public void newPassword(String username, String password) {
        AccountEntity user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }
}
