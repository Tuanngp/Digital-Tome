package com.fpt.swp391.group6.DigitalTome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdsDto {
    private Long id;
    private Long adsId;
    private Long placementId;//
    private Long typeId;//
    private String title; //
    private String content; //
    private String link; //
    private Date startDate; //
    private Date endDate; //
    private String status; //
    private String adsType;
    private String adsPlacement;
    private BigDecimal cost;
    private String imageUrl;
    private MultipartFile file;//
    private String customer;
}
