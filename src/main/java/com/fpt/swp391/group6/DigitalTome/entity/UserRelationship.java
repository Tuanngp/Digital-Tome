package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_relationship", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_first_id", "user_second_id"})
})

public class UserRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_first_id", referencedColumnName = "id", nullable = false)
    private AccountEntity userFirst;

    @ManyToOne
    @JoinColumn(name = "user_second_id", referencedColumnName = "id", nullable = false)
    private AccountEntity userSecond;

    @Column(name = "type")
    private String type;
}
