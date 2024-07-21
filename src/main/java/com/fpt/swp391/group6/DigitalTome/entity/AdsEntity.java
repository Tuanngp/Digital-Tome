package com.fpt.swp391.group6.DigitalTome.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ads")
public class AdsEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @JsonBackReference
    private AccountEntity publisher;

    @ManyToOne
    @JoinColumn(name = "ads_type_id", nullable = false)
    private AdsTypeEntity adsType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdsStatus status = AdsStatus.PENDING;

    public enum AdsStatus {
        PENDING,
        AWAITING_PAYMENT,
        ACTIVE,
        COMPLETED,
        CANCELLED,
        REJECTED
    }
}