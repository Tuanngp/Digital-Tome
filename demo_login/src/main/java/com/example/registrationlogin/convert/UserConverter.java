package com.example.registrationlogin.convert;

import com.example.registrationlogin.dto.UserDto;
import com.example.registrationlogin.entity.User;

public class UserConverter {

    public static User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        return user;
    }

    public static UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
