package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String address;
    private String fullName;
    private String phone;
    private String avatarPath;
    private String description;
    private long point;
    private Integer isNotification;

//    private Date dateOfBirth;
}
