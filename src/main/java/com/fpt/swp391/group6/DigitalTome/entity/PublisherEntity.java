package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publisher")
@Builder
public class PublisherEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private AccountEntity user;

    @Column(name = "name_publisher", nullable = false, unique = true)
    private String namePublisher;

    @Column(name = "certificate_number", nullable = false, unique = true)
    private String certificateNumber;


    @Column(name = "approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean approved;
}