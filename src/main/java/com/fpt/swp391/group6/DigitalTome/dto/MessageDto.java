package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private String sender;
    private String receiver;
    private String content;
    private String status;
    private String avatarSender;
    private String createdDate;

}
