package com.example.registrationlogin.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private Long id;
    private String email;
    @NotEmpty
    private String address;
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String age;

    private String url;
}
