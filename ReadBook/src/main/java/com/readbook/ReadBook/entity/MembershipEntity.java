package com.readbook.ReadBook.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "membership")
public class MembershipEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name="point", nullable = false )
    private long point;

    @OneToMany(mappedBy = "membershipEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<AccountEntity> accountEntities;


}
