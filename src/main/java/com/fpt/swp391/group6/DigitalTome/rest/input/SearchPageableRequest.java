package com.fpt.swp391.group6.DigitalTome.rest.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SearchPageableRequest {
    String keyword;
    List<String> categories;
    List<Integer> years;
    Integer minPoint;
    Integer maxPoint;
    int page;
    int size;
    String sortByValue;
    String sortDirValue;
}
