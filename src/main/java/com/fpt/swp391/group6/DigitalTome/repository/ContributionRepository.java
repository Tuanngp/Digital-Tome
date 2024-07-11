package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository extends JpaRepository<ContributionEntity, Long> {

    boolean existsByAccountEntityIdAndBookEntityId(Long accountId, Long bookId);

    @Query("SELECT c.bookEntity.id FROM ContributionEntity c WHERE c.accountEntity.id = :accountId")
    List<Long> findBookIdsByAccountId(@Param("accountId") Long accountId);
}
