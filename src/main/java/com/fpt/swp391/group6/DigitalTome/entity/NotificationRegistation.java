package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification_registation")
public class NotificationRegistation {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private AccountEntity publisher;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AccountEntity user;

    @Column(name = "received", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean received;
}
