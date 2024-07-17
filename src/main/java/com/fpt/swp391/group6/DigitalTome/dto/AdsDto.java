package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdsDto {
    private Long id;
    private Long adsId;
    private Long placementId;
    private Long typeId;
    private String title;
    private String imageUrl;
    private String adsType;
    private Date startDate;
    private Date endDate;
    private String status;
}
