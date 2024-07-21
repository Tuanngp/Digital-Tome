package com.fpt.swp391.group6.DigitalTome.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.dto.AdsPackageDto;
import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentResponse;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import com.fpt.swp391.group6.DigitalTome.mapper.AdsMapper;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsAssignmentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsPlacementRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsTypeRepository;
import com.fpt.swp391.group6.DigitalTome.utils.ImageUtils;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
    private final PaypalService paypalService;

    @Autowired
    public AdsService(AdsAssignmentRepository adsAssignmentRepository, AdsPlacementRepository adsPlacementRepository, AdsTypeRepository adsTypeRepository, AdsRepository adsRepository, AdsMapper adsMapper, UserService userService, PaypalService paypalService) {
        this.adsAssignmentRepository = adsAssignmentRepository;
        this.adsPlacementRepository = adsPlacementRepository;
        this.adsTypeRepository = adsTypeRepository;
        this.adsRepository = adsRepository;
        this.adsMapper = adsMapper;
        this.userService = userService;
        this.paypalService = paypalService;
    }
//    private String uploadFile(MultipartFile file) {
//        try{
//            var result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
//                    "folder", "/ads",
//                    "use_filename", true,
//                    "unique_filename", true,
//                    "resource_type","auto"
//            ));
//
//            return  result.get("secure_url").toString();
//        } catch (IOException io){
//            throw new RuntimeException("Image upload fail");
//        }
//    }

    private AdsEntity createAds(AdsDto adsDto) throws IOException {
        AccountEntity user = userService.getCurrentLogin();
        AdsTypeEntity adsType = Optional.ofNullable(getAdsType(adsDto.getTypeId()))
                .orElseThrow(() -> new EntityNotFoundException("AdsType not found"));

        AdsEntity ads = adsMapper.toEntity(adsDto);
        ads.setAdsType(adsType);
        ads.setPublisher(user);
//        ads.setImageUrl(uploadFile(adsDto.getFile()));
        ads.setImageUrl(ImageUtils.uploadImage(adsDto.getFile(), "ads"));
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

    public Page<AdsDto> getAdsAssignments(int page, int size, String status) {
        AccountEntity user = userService.getCurrentLogin();
        PageRequest pageRequest = PageRequest.of(page, size);
        if (status.equals("all")) {
            return adsAssignmentRepository.findAllByAds_PublisherOrderByIdDesc(user, pageRequest)
                    .map(adsMapper::toDto);
        }
        AdsEntity.AdsStatus adsStatus = AdsEntity.AdsStatus.valueOf(status);
        return adsAssignmentRepository.findAllByAds_PublisherAndAds_StatusOrderByIdDesc(user, adsStatus, pageRequest)
                .map(adsMapper::toDto);
    }

    public Page<AdsDto> getAllAdsAssignment(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adsAssignmentRepository.findAll(pageRequest)
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

    public void deleteAdsAssignment(Long id) {
        AccountEntity user = userService.getCurrentLogin();
        AdsAssignmentEntity assignment = adsAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AdsAssignment not found"));
        if (!assignment.getAds().getPublisher().getId().equals(user.getId())) {
            throw new EntityNotFoundException("User is not the owner of this AdsAssignment");
        }
        ImageUtils.destroyImage(assignment.getAds().getImageUrl(), "ads");
        adsRepository.delete(assignment.getAds());
        adsAssignmentRepository.delete(assignment);
    }

    private void updateAdsStatus(Long adsId, AdsEntity.AdsStatus status) {
        AdsEntity ads = adsRepository.findById(adsId)
                .orElseThrow(() -> new EntityNotFoundException("Ads not found"));
        ads.setStatus(status);
        adsRepository.save(ads);
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http or https
        String serverName = request.getServerName();     // hostname or IP address
        int serverPort = request.getServerPort();        // port number
        String contextPath = request.getContextPath();   // application name

        if (serverName.equals("localhost")) {
            return scheme + "://" + serverName + ":" + serverPort + contextPath;
        } else {
            return "https://digitaltome.azurewebsites.net" + contextPath;
        }
    }

    public ResponseEntity<?> createPayAndRedirect(Long adsId, String amount, String currency, String description, HttpServletRequest request) {
        try {
            String cancelUrl = getBaseUrl(request) + "/advertisement?status=cancel";
            String successUrl = getBaseUrl(request) + "/advertisement/success?adsId=" + adsId;

            double rate = 25128.0;
            Payment payment = paypalService.createPayment(
                    Double.parseDouble(amount)/rate,
                    currency,
                    "Paypal",
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(com.paypal.api.payments.Links::getHref)
                    .orElseThrow(() -> new RuntimeException("No approval URL found"));

            return ResponseEntity.ok(new PaymentResponse("success", "Payment created", approvalUrl));
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponse("error", "Failed to create payment: " + e.getMessage(), null));
        }
    }

    public String paymentSuccess(String paymentId, String payerId, Long adsId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                updateAdsStatus(adsId, AdsEntity.AdsStatus.ACTIVE);
                return "redirect:/advertisement?status=success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/advertisement?status=error";
    }

    public List<AdsEntity> getAdsHomepage() {
        return adsAssignmentRepository.findByDateBetweenStartDateAndEndDate(new Date()).stream()
                .filter(ads ->  ads.getAds().getStatus().equals(AdsEntity.AdsStatus.ACTIVE) &&
                                ads.getPlacement().getName().equals("Homepage")
                )
                .map(AdsAssignmentEntity::getAds).toList();
    }

    public List<AdsEntity> getAdsFooter() {
        return adsAssignmentRepository.findByDateBetweenStartDateAndEndDate(new Date()).stream()
                .filter(ads ->  ads.getAds().getStatus().equals(AdsEntity.AdsStatus.ACTIVE) &&
                                ads.getPlacement().getName().equals("Footer")
                )
                .map(AdsAssignmentEntity::getAds).toList();
    }

    @Transactional
    public void updateCompletedAdsStatus() {
        List<AdsAssignmentEntity> assignments = adsAssignmentRepository.findAssignmentsWithPastEndDate(new Date());
        for (AdsAssignmentEntity assignment : assignments) {
            AdsEntity ads = assignment.getAds();
            ads.setStatus(AdsEntity.AdsStatus.COMPLETED);
            adsRepository.save(ads);
        }
    }

    public List<AdsEntity> getAdsPopup() {
        return adsAssignmentRepository.findByDateBetweenStartDateAndEndDate(new Date()).stream()
                .filter(ads ->  ads.getAds().getStatus().equals(AdsEntity.AdsStatus.ACTIVE) &&
                                ads.getPlacement().getName().equals("Pop-up")
                )
                .map(AdsAssignmentEntity::getAds).toList();
    }

    public Page<AdsDto> searchAdsAssignments(int page, int size, String keyword, String status) {
        AccountEntity user = userService.getCurrentLogin();
        PageRequest pageRequest = PageRequest.of(page, size);
        if (keyword.trim().isEmpty()) {
            return getAdsAssignments(page, size, status);
        }
        if (status.equals("all")) {
            return adsAssignmentRepository.searchAdsAssignments(user, keyword, pageRequest)
                    .map(adsMapper::toDto);
        }
        AdsEntity.AdsStatus adsStatus = AdsEntity.AdsStatus.valueOf(status);
        return adsAssignmentRepository.searchAdsAssignments(user, keyword, adsStatus, pageRequest)
                .map(adsMapper::toDto);
    }
}
