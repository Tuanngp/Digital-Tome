package com.fpt.swp391.group6.DigitalTome.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RegisterDto
{
    private Long id;
    @NotEmpty(message = "Username should not be empty")
    private String username;


    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;


    @NotEmpty(message = "Password should not be empty")
    private String password;

}

