package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Value
@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ContributionDto {
    Long id;
    Long publisherId;
    String publisherName;
    String publisherAvatarPath;
    String isbn;
    String bookCoverPath;
    String title;
    String description;
    Date publicationDate;
    Date modifiedDate;
}
