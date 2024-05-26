package com.fpt.swp391.group6.DigitalTome.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private int status;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "bio")
    private String bio;

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private long point;

    @Column(name = "activity_point", columnDefinition = "BIGINT DEFAULT 0")
    private long activityPoint;

    @Column(name = "token")
    private String token;

    @Column(name = "token_creation_data")
    private Date tokenCreationData;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "membership_id")
    private MembershipEntity membershipEntity;

    @Column(name = "membership_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date membershipExpiryDate;

    @Column(name = "publisher_certificate")
    private String pulisherCertificate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<CommentEntity> commentEntityList;

    @OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<RateEntity> rateEntityList;

    @OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<ContributionEntity> contributionEntityList;

    @OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<FavoriteEntity> favoriteEntities;

    @OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntityList;

//    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH})
//    private List<NotitifcationEntity> notitifcationEntityList1;
//
//    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH})
//    private List<NotitifcationEntity> notitifcationEntityList2;

}
