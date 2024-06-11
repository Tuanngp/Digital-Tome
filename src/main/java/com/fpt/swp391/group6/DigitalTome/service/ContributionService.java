package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.repository.ContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributionService {
    @Autowired
    private ContributionRepository contributionRepository;

    public void saveContribution(ContributionEntity contribution) {
        contributionRepository.save(contribution);
    }
}
