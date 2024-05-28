//package com.fpt.swp391.group6.DigitalTome.entity;
//
//import jakarta.persistence.*;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "notification")
//public class NotitifcationEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "content")
//    private String content;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumn(name = "publisher_id")
//    private AccountEntity publisher;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH})
//    @JoinColumn(name = "user_id")
//    private AccountEntity user;
//
//    public NotitifcationEntity() {
//    }
//
//    public NotitifcationEntity(Long id, String content, AccountEntity publisher, AccountEntity user) {
//        this.id = id;
//        this.content = content;
//        this.publisher = publisher;
//        this.user = user;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public AccountEntity getPublisher() {
//        return publisher;
//    }
//
//    public void setPublisher(AccountEntity publisher) {
//        this.publisher = publisher;
//    }
//
//    public AccountEntity getUser() {
//        return user;
//    }
//
//    public void setUser(AccountEntity user) {
//        this.user = user;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    @Override
//    public String toString() {
//        return "NotitifcationEntity{" +
//                "id=" + id +
//                ", content='" + content + '\'' +
//                ", publisher=" + publisher +
//                ", user=" + user +
//                '}';
//    }
//}
