package com.example.registrationlogin.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String address;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must be alphabets")
    private String fullName;
    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone must be 9 digits")
    private String phone;
    @NotBlank
    @Pattern(regexp = "^[0-9]$", message = "Age must be digits")
    private String age;
}
