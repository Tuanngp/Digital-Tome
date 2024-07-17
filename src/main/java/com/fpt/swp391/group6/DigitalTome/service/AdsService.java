package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import com.fpt.swp391.group6.DigitalTome.mapper.AdsMapper;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsAssignmentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsPlacementRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdsService {
    private final AdsAssignmentRepository adsAssignmentRepository;
    private final AdsPlacementRepository adsPlacementRepository;
    private final AdsTypeRepository adsTypeRepository;
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;
    private final UserService userService;

    @Autowired
    public AdsService(AdsAssignmentRepository adsAssignmentRepository, AdsPlacementRepository adsPlacementRepository, AdsTypeRepository adsTypeRepository, AdsRepository adsRepository, AdsMapper adsMapper, UserService userService) {
        this.adsAssignmentRepository = adsAssignmentRepository;
        this.adsPlacementRepository = adsPlacementRepository;
        this.adsTypeRepository = adsTypeRepository;
        this.adsRepository = adsRepository;
        this.adsMapper = adsMapper;
        this.userService = userService;
    }

    public AdsEntity saveAds(AdsEntity adsEntity) {
        return adsRepository.save(adsEntity);
    }

    public AdsEntity createAds(AdsDto adsDto) {
        AccountEntity user = userService.getCurrentLogin();
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        if (!user.getRoleEntity().getName().equals("ROLE_PUBLISHER")) {
            throw new EntityNotFoundException("User is not a publisher");
        }
        AdsTypeEntity adsType = Optional.ofNullable(getAdsType(adsDto.getTypeId()))
                .orElseThrow(() -> new EntityNotFoundException("AdsType not found"));

        AdsEntity ads = adsMapper.toEntity(adsDto);
        ads.setAdsType(adsType);
        ads.setPublisher(user);
        return adsRepository.save(ads);
    }

    public AdsTypeEntity getAdsType(Long id) {
        return adsTypeRepository.findById(id).orElse(null);
    }

    public AdsPlacementEntity getAdsPlacement(Long id) {
        return adsPlacementRepository.findById(id).orElse(null);
    }

    public AdsAssignmentEntity createAdsAssignment(AdsDto adsDto) {
        AdsPlacementEntity placement = Optional.ofNullable(getAdsPlacement(adsDto.getPlacementId()))
                .orElseThrow(() -> new EntityNotFoundException("AdsPlacement not found"));

        AdsEntity savedAds = createAds(adsDto);

        AdsAssignmentEntity assignment = AdsAssignmentEntity.builder()
                .ads(savedAds)
                .placement(placement)
                .startDate(adsDto.getStartDate())
                .endDate(adsDto.getEndDate())
                .build();
        return adsAssignmentRepository.save(assignment);
    }

    public List<AdsDto> getAdsAssignments() {
        AccountEntity user = userService.getCurrentLogin();
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        if (!user.getRoleEntity().getName().equals("ROLE_PUBLISHER")) {
            throw new EntityNotFoundException("User is not a publisher");
        }
        return adsAssignmentRepository.findAllByAds_Publisher(user).stream()
                .map(adsMapper::toDto)
                .toList();
    }

    public List<AdsDto> getAds() {
        return adsRepository.findAll().stream()
                .map(adsMapper::toDto)
                .toList();
    }

    public List<AdsPlacementEntity> getAdsPlacements() {
        return adsPlacementRepository.findAll();
    }

    public List<AdsTypeEntity> getAdsTypes() {
        return adsTypeRepository.findAll();
    }
}
