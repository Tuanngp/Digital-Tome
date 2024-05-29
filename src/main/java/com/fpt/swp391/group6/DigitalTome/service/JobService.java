package com.fpt.swp391.group6.DigitalTome.service;


import com.fpt.swp391.group6.DigitalTome.entity.JobExecutionEntity;
import com.fpt.swp391.group6.DigitalTome.repository.JobExecutionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JobService {

    private final JobExecutionRepository jobExecutionRepository;

    @Autowired
    public JobService(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }

    @Transactional
    public void updateLastExecutionTime(String jobName) {
         JobExecutionEntity jobExecutionEntity = jobExecutionRepository.findByJobName(jobName)
               .orElse(new JobExecutionEntity());
          jobExecutionEntity.setJobName(jobName);
          jobExecutionEntity.setLastExecutionTime(new Date());
          jobExecutionRepository.save(jobExecutionEntity);
          System.out.println("Saved JobExecutionEntity: " + jobExecutionEntity);


//        JobExecutionEntity jobExecution = new JobExecutionEntity(jobName, new Date());
//        jobExecutionRepository.save(jobExecution);
    }

    public Date getLastExecutionTime(String jobName) {
          Optional<JobExecutionEntity> jobExecutionEntity = jobExecutionRepository.findByJobName(jobName);
          return jobExecutionEntity.map(JobExecutionEntity::getLastExecutionTime).orElse(new Date(0));

//        JobExecutionEntity jobExecution = jobExecutionRepository.findTopByJobNameOrderByLastExecutionTimeDesc(jobName);
//        return jobExecution != null ? jobExecution.getLastExecutionTime() : new Date(0);
    }
}

