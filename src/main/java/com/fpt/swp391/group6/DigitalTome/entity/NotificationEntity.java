package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The NotificationEntity class is an entity model object. It represents the notification entity in the database.
 * The NotificationEntity class is mapped to the notification table in the database.
 * The NotificationEntity class contains the following fields:
 * - id: the notification's id
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
@Entity
public class NotificationEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private AccountEntity receiver;

    @Column(name = "seen", nullable = false)
    private boolean seen;
}
