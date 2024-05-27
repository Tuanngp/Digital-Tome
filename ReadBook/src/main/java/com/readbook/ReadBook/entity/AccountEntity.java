package com.readbook.ReadBook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class AccountEntity
{
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

    @Column(name = "address",length = 255)
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

    @Column(name = "token_creation_data")
    private String tokenCreationData;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime tokenCreationDate;


    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "membership_id")
    private MembershipEntity membershipEntity;

    @Column(name = "membership_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date membershipExpiryDate; // ngÃ y thÃ nh viÃªn háº¿t háº¡n

    @Column(name = "publisher_certificate")
    private String pulisherCertificate; // giÃ¢y chá»©ng nháº­n nhÃ  xuáº¥t báº£n


    @ManyToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;



    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<CommentEntity> commentEntityList; // BÃ¬nh luáº­n


    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<RateEntity> rateEntityList;

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<ContributionEntity> contributionEntityList;  // Ä‘Ã³ng gÃ³p

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    private List<FavoriteEntity> favoriteEntities; // yÃªu thÃ­ch

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntityList; // thanh toÃ¡n

//    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH})
//    private List<NotitifcationEntity> notitifcationEntityList1;
//
//    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.REFRESH, CascadeType.DETACH})
//    private List<NotitifcationEntity> notitifcationEntityList2;


    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;  // ngÃ y táº¡o

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedDate;  // ngÃ y sá»­a Ä‘á»•i

    @Column(name = "modified_by")
    private Long modifiedBy;  // Ä‘Æ°á»£c sá»­a bá»Ÿi

}

