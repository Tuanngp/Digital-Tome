package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import com.fpt.swp391.group6.DigitalTome.mapper.ContributionMapper;
import com.fpt.swp391.group6.DigitalTome.repository.ContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributionService {

    private ContributionRepository contributionRepository;
    private ContributionMapper contributionMapper;

    @Autowired
    public ContributionService(ContributionRepository contributionRepository, ContributionMapper contributionMapper) {
        this.contributionRepository = contributionRepository;
        this.contributionMapper = contributionMapper;
    }

    public List<ContributionDto> getContributionByStatus(int status, Pageable pageable){
        return  this.contributionRepository
                .findByBookEntity_Status(status, pageable)
                .stream()
                .map(contributionMapper::toDto)
                .collect(Collectors.toList());
    }

    public int countContributionByStatus(int status){
        return  this.contributionRepository.countByBookEntity_Status(status);
    }

    public void updateModifiedDateByBookEntity_ISBN( String isbn){
        this.contributionRepository.updateModifiedDateByBookEntity_ISBN(isbn);
    }
}
