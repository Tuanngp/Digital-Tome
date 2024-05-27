package com.readbook.ReadBook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private Long id;
    private String email;
    private String address;
    private String fullName;
    private String phone;
    private String avatarPath;
    private String description;

}

