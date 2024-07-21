package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ads_assignments")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdsAssignmentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ads_id", nullable = false)
    private AdsEntity ads;

    @ManyToOne
    @JoinColumn(name = "placement_id", nullable = false)
    private AdsPlacementEntity placement;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "cost", precision = 19, scale = 2)
    private BigDecimal cost;
}
