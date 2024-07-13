package com.fpt.swp391.group6.DigitalTome.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private String language;
    private String bookPath;
}
