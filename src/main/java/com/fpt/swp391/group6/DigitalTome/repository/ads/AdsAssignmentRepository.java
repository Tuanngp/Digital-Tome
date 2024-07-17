package com.fpt.swp391.group6.DigitalTome.repository.ads;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdsAssignmentRepository extends JpaRepository<AdsAssignmentEntity, Long> {
    List<AdsAssignmentEntity> findAllByAds_Publisher(AccountEntity account);
}
