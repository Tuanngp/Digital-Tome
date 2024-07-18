package com.fpt.swp391.group6.DigitalTome.dto;

import com.fpt.swp391.group6.DigitalTome.entity.AdsPlacementEntity;
import com.fpt.swp391.group6.DigitalTome.entity.AdsTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdsPackageDto {
    private AdsPlacementEntity adsPlacement;
    private List<AdsTypeEntity> adsTypes;
}
