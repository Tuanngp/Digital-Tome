package com.fpt.swp391.group6.DigitalTome.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fpt.swp391.group6.DigitalTome.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class AccountEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", length = 50)
    private String fullname;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phone",length = 11)
    private String phone;

    @Column(name = "email",length = 50)
    private String email;

    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private int status;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Column(name = "description")
    private String description;

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private long point;

    @Column(name = "activity_point",  columnDefinition = "BIGINT DEFAULT 0")
    private long activityPoint;

    @Column(name = "token")
    private String token;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "membership_id")
    private MembershipEntity membershipEntity;

    @Column(name = "membership_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date membershipExpiryDate;

    @Column(name = "name_publisher", nullable = true, unique = true)
    private String namePublisher;

    @Column(name = "certificate_number", nullable = true, unique = true)
    private String certificateNumber;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JsonManagedReference
    private List<CommentEntity> commentEntityList;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<RateEntity> rateEntityList;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<ContributionEntity> contributionEntityList;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<FavoriteEntity> favoriteEntities;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntityList;

    @OneToMany(mappedBy = "userFirst", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    private Set<UserRelationship> relationshipsInitiated;

    @OneToMany(mappedBy = "userSecond", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    private Set<UserRelationship> relationshipsReceived;


//    @OneToMany(mappedBy = "publisher", cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
//    private Set<NotificationEntity> registerations;

//    @OneToMany(mappedBy = "publisher", cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
//    private Set<NotificationEntity> notifications;

    @OneToMany(mappedBy = "user", cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    private Set<NotificationEntity> registrationsReceived;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JsonManagedReference
    private Set<NotificationEntity> notifications;

}