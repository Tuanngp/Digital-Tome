package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateEntity, Long>{
    RateEntity findByAccountEntity_IdAndBookEntity_Id(Long userId, Long bookId);
    RateEntity findByBookEntity_Id(Long bookId);
    RateEntity findByAccountEntity_Id(Long userId);
}
