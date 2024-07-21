package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.dto.AdsPackageDto;
import com.fpt.swp391.group6.DigitalTome.dto.paymentResponse.PaymentResponse;
import com.fpt.swp391.group6.DigitalTome.entity.AdsEntity;
import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import com.fpt.swp391.group6.DigitalTome.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdsRest {
    private final AdsService adsService;

    public AdsRest(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    public ResponseEntity<Page<AdsDto>> searchAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String status) {
        return ResponseEntity.ok(adsService.searchAdsAssignments(page, size, keyword, status));
    }

    @GetMapping("infor")
    public ResponseEntity<AdsDto> getAdsInfo(
            @RequestParam Long id) {
        return ResponseEntity.ok(adsService.findById(id));
    }

    @PutMapping("/acceptation")
    public ResponseEntity<?> acceptAds(@RequestParam Long adsId) {
        return adsService.acceptAds(adsId) ;
    }

    @PutMapping("/rejection")
    public ResponseEntity<?> reject(@RequestParam Long adsId) {
        return adsService.rejectAds(adsId);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<AdsDto>> filterAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = " ") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "desc") String orderByDate) {
        return ResponseEntity.ok(adsService.filterAds(page, size, keyword, orderByDate, status));
    }

    @GetMapping("/ads-package")
    public ResponseEntity<List<AdsPackageDto>> getAdsPackage() {
        return ResponseEntity.ok(adsService.getAdsPackages());
    }

    @PostMapping(value="/save", consumes = "multipart/form-data")
    public ResponseEntity<AdsDto> saveAds(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("link") String link,
            @RequestParam("placementId") Long placementId,
            @RequestParam("typeId") Long typeId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam("cost") String costString,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Date sdate = DateUtils.convertToDateViaInstant(startDate);
        Date edate = DateUtils.convertToDateViaInstant(endDate);
        String cost = costString.replaceAll("\\D", "");
        AdsDto adsDto = AdsDto.builder()
                .placementId(placementId)
                .typeId(typeId)
                .title(title)
                .content(content)
                .link(link)
                .startDate(sdate)
                .endDate(edate)
                .status(AdsEntity.AdsStatus.PENDING.toString())
                .file(file)
                .cost(new BigDecimal(cost))
                .build();
        return ResponseEntity.ok(adsService.createAdsAssignment(adsDto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable Long id) {
        adsService.deleteAdsAssignment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(
            @RequestParam("adsId") Long adsId,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description,
            HttpServletRequest request
    ) {
        return adsService.createPayAndRedirect(adsId, amount, currency, description, request);
    }
}

