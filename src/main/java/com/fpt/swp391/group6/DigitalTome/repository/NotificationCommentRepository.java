//package com.fpt.swp391.group6.DigitalTome.repository;
//
//import com.fpt.swp391.group6.DigitalTome.entity.NotificationCommentEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface NotificationCommentRepository extends JpaRepository<NotificationCommentEntity, Long> {
//    List<NotificationCommentEntity> findByAccount_IdAndIsReadFalse(Long accountId);
//}