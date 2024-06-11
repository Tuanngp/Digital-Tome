package com.fpt.swp391.group6.DigitalTome.rest.output;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AbstractOutput<T> {
    int currentPage;
    int totalPages;
    List<T> listResults;
}
