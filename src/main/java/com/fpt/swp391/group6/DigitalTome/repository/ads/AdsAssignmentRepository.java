package com.fpt.swp391.group6.DigitalTome.repository.ads;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsAssignmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AdsAssignmentRepository extends JpaRepository<AdsAssignmentEntity, Long> {
    List<AdsAssignmentEntity> findAllByAds_Publisher(AccountEntity account);
    Page<AdsAssignmentEntity> findAllByAds_Publisher(AccountEntity publisher, Pageable pageable);

    @Query("SELECT a FROM AdsAssignmentEntity a WHERE :date BETWEEN a.startDate AND a.endDate")
    List<AdsAssignmentEntity> findByDateBetweenStartDateAndEndDate(@Param("date") Date date);
}
