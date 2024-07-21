package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.UserMapper;
import com.fpt.swp391.group6.DigitalTome.rest.output.UserOutput;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRest {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserRest(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public UserOutput getUser(@RequestParam String username) {
        UserOutput result = new UserOutput();
        result.setUserDto(userMapper.toDto(userService.findByUsername(username)));
        return result;
    }

    @GetMapping("/{id}")
    public UserOutput getUser(@PathVariable Long id) {
        UserOutput result = new UserOutput();
        result.setUserDto(userMapper.toDto(userService.findById(id)));
        return result;
    }

    // get UserOutput by email, phone, username, or id
    @GetMapping("/search")
    public UserOutput searchUser(@RequestParam String keyword) {
        UserOutput result = new UserOutput();
        result.setUserDto(userMapper.toDto(userService.search(keyword)));
        return result;
    }


}
