package com.fpt.swp391.group6.DigitalTome.rest.output;


import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BookOutput {
    BookDetailDto bookDetailDto;
}
