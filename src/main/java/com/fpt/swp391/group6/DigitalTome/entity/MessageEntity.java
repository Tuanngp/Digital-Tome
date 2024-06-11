package com.fpt.swp391.group6.DigitalTome.entity;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageEntity {
    private String message;
    private AccountEntity sender;
    private AccountEntity receiver;
    private String time;
    private boolean isRead;
    private Status status;
}

enum Status {
    JOIN, LEAVE, MESSAGE
}