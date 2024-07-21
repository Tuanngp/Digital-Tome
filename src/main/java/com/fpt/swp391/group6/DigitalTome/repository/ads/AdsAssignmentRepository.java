package com.fpt.swp391.group6.DigitalTome.repository.ads;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsAssignmentEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AdsAssignmentRepository extends JpaRepository<AdsAssignmentEntity, Long> {
    List<AdsAssignmentEntity> findAllByAds_Publisher(AccountEntity account);

    Page<AdsAssignmentEntity> findAllByAds_PublisherOrderByIdDesc(AccountEntity publisher, Pageable pageable);

    @Query("SELECT a FROM AdsAssignmentEntity a WHERE :date BETWEEN a.startDate AND a.endDate")
    List<AdsAssignmentEntity> findByDateBetweenStartDateAndEndDate(@Param("date") Date date);

    @Query("SELECT a FROM AdsAssignmentEntity a WHERE a.endDate < :currentDate")
    List<AdsAssignmentEntity> findAssignmentsWithPastEndDate(@Param("currentDate") Date currentDate);

    Page<AdsAssignmentEntity> findAllByAds_PublisherAndAds_StatusOrderByIdDesc(AccountEntity user, AdsEntity.AdsStatus adsStatus, Pageable pageable);

    @Query("SELECT a FROM AdsAssignmentEntity a JOIN a.ads ad WHERE a.ads.publisher = :user AND " +
            "(LOWER(ad.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(ad.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<AdsAssignmentEntity> searchAdsAssignments(@Param("user") AccountEntity user,
                                                   @Param("keyword") String keyword,
                                                   Pageable pageRequest);

    @Query("SELECT a FROM AdsAssignmentEntity a JOIN a.ads ad WHERE a.ads.publisher = :user AND " +
            "(LOWER(ad.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(ad.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "ad.status = :adsStatus")
    Page<AdsAssignmentEntity> searchAdsAssignments(@Param("user") AccountEntity user,
                                                   @Param("keyword") String keyword,
                                                   @Param("adsStatus") AdsEntity.AdsStatus adsStatus,
                                                   Pageable pageRequest);

    @Query("SELECT a from AdsAssignmentEntity a where a.ads.id = :adsId ")
    Optional<AdsAssignmentEntity> findByAdsId(@Param("adsId") Long adsId);
}
