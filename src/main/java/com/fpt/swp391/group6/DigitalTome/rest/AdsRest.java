package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.AdsDto;
import com.fpt.swp391.group6.DigitalTome.entity.AdsPlacementEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsTypeEntity;
import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdsRest {
    private final AdsService adsService;

    public AdsRest(AdsService adsService) {
        this.adsService = adsService;
    }

    @GetMapping
    public ResponseEntity<List<AdsDto>> getAds() {
        return ResponseEntity.ok(adsService.getAdsAssignments());
    }

    @GetMapping("/placements")
    public ResponseEntity<List<AdsPlacementEntity>> getAdsPlacements() {
        return ResponseEntity.ok(adsService.getAdsPlacements());
    }

    @GetMapping("/types")
    public ResponseEntity<List<AdsTypeEntity>> getAdsTypes() {
        return ResponseEntity.ok(adsService.getAdsTypes());
    }
}
