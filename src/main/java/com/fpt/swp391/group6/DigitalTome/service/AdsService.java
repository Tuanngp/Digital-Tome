package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.dto.AdsPackageDto;
import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentResponse;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import com.fpt.swp391.group6.DigitalTome.mapper.AdsMapper;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsAssignmentRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsPlacementRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ads.AdsTypeRepository;
import com.fpt.swp391.group6.DigitalTome.rest.input.EmailRequest;
import com.fpt.swp391.group6.DigitalTome.utils.ImageUtils;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    private final EmailService emailService;

    @Autowired
    public AdsService(AdsAssignmentRepository adsAssignmentRepository, AdsPlacementRepository adsPlacementRepository, AdsTypeRepository adsTypeRepository, AdsRepository adsRepository, AdsMapper adsMapper, UserService userService, PaypalService paypalService, EmailService emailService) {
        this.adsAssignmentRepository = adsAssignmentRepository;
        this.adsPlacementRepository = adsPlacementRepository;
        this.adsTypeRepository = adsTypeRepository;
        this.adsRepository = adsRepository;
        this.adsMapper = adsMapper;
        this.userService = userService;
        this.paypalService = paypalService;
        this.emailService = emailService;
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

    public ResponseEntity<?> rejectAds(Long adsId){
        try {
            updateAdsStatus(adsId, AdsEntity.AdsStatus.REJECTED);
            EmailRequest emailRequest = new EmailRequest();
            AdsEntity adsEntity = this.adsRepository.findById(adsId).orElse(null);
            emailRequest.setEmailTo(adsEntity.getPublisher().getEmail());
            emailRequest.setSubject("Advertisement Rejection");
            emailRequest.setMessage("Dear ,"+adsEntity.getPublisher().getUsername()+"\n" +
                    "\n" +
                    "Thank you for your recent advertisement registration. After a thorough review, we regret to inform you that your advertisement cannot be approved as it does not meet our current criteria. We encourage you to review our advertising guidelines and consider revising your submission to better align with our requirements. We appreciate your interest in partnering with us and are happy to provide further assistance if needed. Please feel free to reach out with any questions or concerns.\n" +
                    "\n" +
                    "Thank you for your understanding.\n" +
                    "\n" +
                    "Best regards,\n" +
                    "Digital Tome");
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> acceptAds(Long adsId){
        try {
            updateAdsStatus(adsId, AdsEntity.AdsStatus.AWAITING_PAYMENT);
            EmailRequest emailRequest = new EmailRequest();
            AdsEntity adsEntity = this.adsRepository.findById(adsId).orElse(null);
            emailRequest.setEmailTo(adsEntity.getPublisher().getEmail());
            emailRequest.setSubject("Advertisement Acception");
            emailRequest.setMessage("Dear ,"+adsEntity.getPublisher().getUsername()+"\n" +
                    "\n" +
                    "We are pleased to inform you that your advertisement has been approved and will be featured with Digital Tome. Your submission meets our criteria and aligns well with our advertising standards. We look forward to showcasing your advertisement and working together to achieve great results. Should you need any further assistance or have any questions regarding the next steps, please do not hesitate to reach out to us.\n" +
                    "\n" +
                    "Thank you for choosing to advertise with us.\n" +
                    "\n" +
                    "Best regards,\n" +
                    "Digital Tome\n");
            emailService.sendEmail(emailRequest);
            return ResponseEntity.ok().build();
        }catch(Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    /*public void rejectAds(Long adsId){
        updateAdsStatus(adsId, AdsEntity.AdsStatus.REJECTED);
    }*/


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

/*    public List<AdsDto> filterAds(int page, int size, String keyword, String orderByDate, String status) {
        Pageable pageable = PageRequest.of(page, size);
        List<AdsEntity> rs = adsRepositoryCustom.filterAds(pageable, keyword, orderByDate, status);
        return rs.stream().map(adsMapper::toDto).collect(Collectors.toList());
    }*/


    public AdsDto findById(Long id){
        return adsAssignmentRepository.findByAdsId(id)
                .map(adsMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Requested advertisement not found"));
    }

    public Page<AdsDto> filterAds(int page, int size, String keyword, String orderByDate, String status) {
        PageRequest pageable = PageRequest.of(page, size);
        Specification<AdsEntity> spec = filterAdsSpecification(keyword, status, orderByDate);
        return adsRepository.findAll(spec, pageable).map(adsMapper::toDto);
    }

    private  Specification<AdsEntity> filterAdsSpecification(String keyword, String status, String orderByDate) {
        return (Root<AdsEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
                Predicate contentPredicate = criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
                predicates.add(criteriaBuilder.or(titlePredicate, contentPredicate));
            }

            if (orderByDate != null && !orderByDate.isEmpty()) {
                if (orderByDate.equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(root.get("createdDate")));
                } else if (orderByDate.equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
