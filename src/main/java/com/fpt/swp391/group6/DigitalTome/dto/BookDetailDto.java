package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDetailDto {
     String isbn;
     String title;
     String description;
     String language;
     String bookPath;
     String bookCover;
     Date publicationDate;
     int point;
     List<String> authors;
     List<String> categories;
}
