package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.MembershipEntity;
import com.fpt.swp391.group6.DigitalTome.service.MembershipService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberShipController {

    private final MembershipService membershipService;
    private final UserService userService;

    @PostMapping("/api/deductPoints")
    public ResponseEntity<String> deductPoints(
            @RequestParam("amount") Long amount,
            @RequestParam("planType") String planType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        AccountEntity accountEntity = userService.findByUsername(username);

        if (accountEntity.getPoint() < amount) {
            return ResponseEntity.badRequest().body("Not enough points");
        }

        accountEntity.setPoint(accountEntity.getPoint() - amount);

        if (!canUpgrade(accountEntity, planType)) {
            return ResponseEntity.badRequest().body("Cannot upgrade to this plan");
        }
        membershipService.processMembershipUpgrade(accountEntity, planType);
        return ResponseEntity.ok("Successfully upgraded to " + planType + " plan");
    }

    private boolean canUpgrade(AccountEntity accountEntity, String newMembership) {
        MembershipEntity currentMembership = accountEntity.getMembershipEntity();
        if (currentMembership == null || "basic".equals(currentMembership.getName())) {
            return true;
        } else if ("standard".equals(currentMembership.getName())) {
            return "premium".equals(newMembership);
        }
        return false;
    }
}
