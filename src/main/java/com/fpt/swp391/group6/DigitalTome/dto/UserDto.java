package com.fpt.swp391.group6.DigitalTome.dto;

import com.fpt.swp391.group6.DigitalTome.Validation.ValidDateOfBirth;
import com.fpt.swp391.group6.DigitalTome.enums.Gender;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String address;


    @Pattern(regexp = "^[\\p{L} ]+$", message = "Name should contain only alphabetic characters and spaces")
    private String fullName;

    @Pattern(regexp = "^\\d{8,11}$", message = "Phone number must be between 8 and 11 digits")
    private String phone;

    private String avatarPath;
    private String description;
    private long point;

    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @PastOrPresent(message = "Date of birth must be greater than 1950 and less than current")
    @ValidDateOfBirth
    private Date dateOfBirth;

}
