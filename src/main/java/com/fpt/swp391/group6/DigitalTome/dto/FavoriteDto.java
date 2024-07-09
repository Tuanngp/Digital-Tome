package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteDto {
    private Long id;
    private Long accountId;
    private Long bookId;
}
