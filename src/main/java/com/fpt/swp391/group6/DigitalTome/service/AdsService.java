package com.fpt.swp391.group6.DigitalTome.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.dto.AdsPackageDto;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import com.fpt.swp391.group6.DigitalTome.mapper.AdsMapper;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsAssignmentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsPlacementRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
    private final Cloudinary cloudinary;

    @Autowired
    public AdsService(AdsAssignmentRepository adsAssignmentRepository, AdsPlacementRepository adsPlacementRepository, AdsTypeRepository adsTypeRepository, AdsRepository adsRepository, AdsMapper adsMapper, UserService userService, Cloudinary cloudinary) {
        this.adsAssignmentRepository = adsAssignmentRepository;
        this.adsPlacementRepository = adsPlacementRepository;
        this.adsTypeRepository = adsTypeRepository;
        this.adsRepository = adsRepository;
        this.adsMapper = adsMapper;
        this.userService = userService;
        this.cloudinary = cloudinary;
    }

    public AdsDto createAdsAssignment(AdsDto adsDto) throws IOException {
        AdsPlacementEntity placement = Optional.ofNullable(getAdsPlacement(adsDto.getPlacementId()))
                .orElseThrow(() -> new EntityNotFoundException("AdsPlacement not found"));

        AdsEntity savedAds = createAds(adsDto);

        AdsAssignmentEntity assignment = AdsAssignmentEntity.builder()
                .ads(savedAds)
                .placement(placement)
                .startDate(adsDto.getStartDate())
                .endDate(adsDto.getEndDate())
                .cost(adsDto.getCost())
                .build();
        return adsMapper.toDto(adsAssignmentRepository.save(assignment));
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

    public Page<AdsDto> getAdsAssignments(int page, int size) {
//        AccountEntity user = userService.getCurrentLogin();
//        if (user == null) {
//            throw new EntityNotFoundException("User not found");
//        }
//        if (!user.getRoleEntity().getName().equals("ROLE_PUBLISHER")) {
//            throw new EntityNotFoundException("User is not a publisher");
//        }
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return adsAssignmentRepository.findAllByAds_Publisher(user, pageRequest)
//                .map(adsMapper::toDto);
        return adsAssignmentRepository.findAll(PageRequest.of(page, size))
                .map(adsMapper::toDto);
    }


    public List<AdsPackageDto> getAdsPackages() {
        List<AdsPlacementEntity> placements = getAdsPlacements();
        List<AdsTypeEntity> types = getAdsTypes();
        return placements.stream()
                .map(placement -> AdsPackageDto.builder()
                        .adsPlacement(placement)
                        .adsTypes(types)
                        .build())
                .toList();
    }

    private String uploadFile(MultipartFile file) throws IOException {
        try{
            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "/ads",
                    "use_filename", true,
                    "unique_filename", true,
                    "resource_type","auto"
            ));

            return  result.get("secure_url").toString();
        } catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    private AdsEntity createAds(AdsDto adsDto) throws IOException {
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
        ads.setImageUrl(uploadFile(adsDto.getFile()));
        return adsRepository.save(ads);
    }

    private AdsTypeEntity getAdsType(Long id) {
        return adsTypeRepository.findById(id).orElse(null);
    }

    private AdsPlacementEntity getAdsPlacement(Long id) {
        return adsPlacementRepository.findById(id).orElse(null);
    }

    private List<AdsPlacementEntity> getAdsPlacements() {
        return adsPlacementRepository.findAll();
    }

    private List<AdsTypeEntity> getAdsTypes() {
        return adsTypeRepository.findAll();
    }
}
