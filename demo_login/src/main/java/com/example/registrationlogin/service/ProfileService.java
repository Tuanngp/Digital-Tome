package com.example.registrationlogin.service;

import com.example.registrationlogin.dto.ProfileDto;
import com.example.registrationlogin.entity.User;
import com.example.registrationlogin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    private UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileDto findViewProfile(String id){
        return userRepository.findByUser(id);
    }

    public void updateProfile(ProfileDto profileDto){
        Optional<User> userOptional = userRepository.findById(profileDto.getId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setFullName(profileDto.getFullName());
            user.setPhone(profileDto.getPhone());
            user.setAddress(profileDto.getAddress());
            user.setAge(profileDto.getAge());
            userRepository.save(user);
        }
    }
}
