
package com.fpt.swp391.group6.DigitalTome.rest.input;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SearchRequest {
    String keyword;
    List<String> categories;
    List<Integer> years;
    Integer minPoint;
    Integer maxPoint;
}
