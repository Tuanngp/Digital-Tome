package com.fpt.swp391.group6.DigitalTome.scheduler;

import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AdsScheduler {
    private final AdsService adsService;

    @Autowired
    public AdsScheduler(AdsService adsService) {
        this.adsService = adsService;
    }

    @Scheduled(cron = "*/10 * * * * *")
//    @Scheduled(cron = "0 0 0 * * *")
    public void updateAdsStatus() {
        adsService.updateCompletedAdsStatus();
    }
}
