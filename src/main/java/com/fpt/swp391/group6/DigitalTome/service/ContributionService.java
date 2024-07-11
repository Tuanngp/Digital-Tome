package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.repository.ContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService {
    @Autowired
    private ContributionRepository contributionRepository;

    public void saveContribution(ContributionEntity contribution) {
        contributionRepository.save(contribution);
    }

    public boolean isAuthorOfBook(Long accountId, Long bookId) {
        return contributionRepository.existsByAccountEntityIdAndBookEntityId(accountId, bookId);
    }

    public List<Long> getBookIdsByAccountId(Long accountId) {
        return contributionRepository.findBookIdsByAccountId(accountId);
    }
}
