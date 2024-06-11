package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :accountId ORDER BY n.createdAt ASC ")
    List<NotificationEntity> findByUserIdOrderByIdDesc(Long accountId);
}
