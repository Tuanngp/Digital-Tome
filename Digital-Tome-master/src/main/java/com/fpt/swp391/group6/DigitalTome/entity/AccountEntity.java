package com.fpt.swp391.group6.DigitalTome.entity;
import com.fpt.swp391.group6.DigitalTome.entity.CommentEntity;
import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RateEntity;
import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MembershipEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.entity.RoleEntity;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", length = 50)
    private String name;

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

    @Column(name = "bio")
    private String bio;

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private long point;

    @Column(name = "activity_point",  columnDefinition = "BIGINT DEFAULT 0")
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

    @OneToMany(mappedBy = "accountEntity",fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
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
    private Date createdDate;

    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(name = "modified_by")
    private Long modifiedBy;



    public AccountEntity() {
    }

    public AccountEntity(String username, String password, String name, String address, String phone, Date dateOfBirth, String email, String avatarPath, String bio, long point, long activityPoint, MembershipEntity membershipEntity, Date membershipExpiryDate, RoleEntity roleEntity, Date createdDate, Long createdBy, Date modifiedDate, Long modifiedBy) {

        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.avatarPath = avatarPath;
        this.bio = bio;
        this.point = point;
        this.activityPoint = activityPoint;
        this.membershipEntity = membershipEntity;
        this.membershipExpiryDate = membershipExpiryDate;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
        this.roleEntity = roleEntity;
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public void setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<CommentEntity> getCommentEntityList() {
        return commentEntityList;
    }

    public void setCommentEntityList(List<CommentEntity> commentEntityList) {
        this.commentEntityList = commentEntityList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenCreationData() {
        return tokenCreationData;
    }

    public void setTokenCreationData(Date tokenCreationData) {
        this.tokenCreationData = tokenCreationData;
    }

    public String getPulisherCertificate() {
        return pulisherCertificate;
    }

    public void setPulisherCertificate(String pulisherCertificate) {
        this.pulisherCertificate = pulisherCertificate;
    }

    public List<RateEntity> getRateEntityList() {
        return rateEntityList;
    }

    public void setRateEntityList(List<RateEntity> rateEntityList) {
        this.rateEntityList = rateEntityList;
    }

    public List<FavoriteEntity> getFavoriteEntities() {
        return favoriteEntities;
    }

    public void setFavoriteEntities(List<FavoriteEntity> favoriteEntities) {
        this.favoriteEntities = favoriteEntities;
    }

    public List<PaymentEntity> getPaymentEntityList() {
        return paymentEntityList;
    }

    public void setPaymentEntityList(List<PaymentEntity> paymentEntityList) {
        this.paymentEntityList = paymentEntityList;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }



    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getActivityPoint() {
        return activityPoint;
    }

    public void setActivityPoint(long activityPoint) {
        this.activityPoint = activityPoint;
    }

    public MembershipEntity getMembershipEntity() {
        return membershipEntity;
    }

    public void setMembershipEntity(MembershipEntity membershipEntity) {
        this.membershipEntity = membershipEntity;
    }

    public Date getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public void setMembershipExpiryDate(Date membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public List<ContributionEntity> getContributionEntityList() {
        return contributionEntityList;
    }

    public void setContributionEntityList(List<ContributionEntity> contributionEntityList) {
        this.contributionEntityList = contributionEntityList;
    }

//    public List<NotitifcationEntity> getNotitifcationEntityList() {
//        return notitifcationEntityList1;
//    }
//
//    public void setNotitifcationEntityList(List<NotitifcationEntity> notitifcationEntityList) {
//        this.notitifcationEntityList1 = notitifcationEntityList;
//    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
