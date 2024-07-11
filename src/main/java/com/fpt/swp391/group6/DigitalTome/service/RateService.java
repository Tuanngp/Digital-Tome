package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.RateEntity;
import com.fpt.swp391.group6.DigitalTome.repository.RateRepository;

public class RateService
{
    private final RateRepository rateRepository;

    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public RateEntity findByBookIdAndUserId(Long bookId, Long userId) {
        return rateRepository.findByAccountEntity_IdAndBookEntity_Id(userId, bookId);
    }

    public RateEntity findByBookId(Long bookId) {
        return rateRepository.findByBookEntity_Id(bookId);
    }

    public RateEntity findByUserId(Long userId) {
        return rateRepository.findByAccountEntity_Id(userId);
    }
}
