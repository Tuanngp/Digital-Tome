package com.fpt.swp391.group6.DigitalTome.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PublisherDTO {
    private Long id;
    private Long userId;
    private String email;
    private String username;
    private String namePublisher;
    private String certificateNumber;
    private boolean approved;
}
