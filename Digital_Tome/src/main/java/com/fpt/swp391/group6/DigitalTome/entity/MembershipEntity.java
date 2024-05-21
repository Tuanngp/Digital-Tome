package com.fpt.swp391.group6.DigitalTome.entity;

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

    public MembershipEntity() {
    }

    public MembershipEntity(String name, long point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoint() {
        return point;
    }

    public List<AccountEntity> getAccounts() {
        return accountEntities;
    }

    public void setAccounts(List<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MembershipEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", point=" + point +
                '}';
    }
}
