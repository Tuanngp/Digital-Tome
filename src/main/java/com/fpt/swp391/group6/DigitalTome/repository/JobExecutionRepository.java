package com.fpt.swp391.group6.DigitalTome.repository;

import com.fpt.swp391.group6.DigitalTome.entity.JobExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecutionEntity, Long> {
    Optional<JobExecutionEntity> findByJobName(String jobName);
    JobExecutionEntity findTopByJobNameOrderByLastExecutionTimeDesc(String jobName);
}
