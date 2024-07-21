package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.appRegister.ApprovalRequest;
import com.fpt.swp391.group6.DigitalTome.dto.appRegister.ApprovalResponse;
import com.fpt.swp391.group6.DigitalTome.service.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/approve")
    public ResponseEntity<ApprovalResponse> approveUser(@RequestBody ApprovalRequest request) {
        if (request.isAction()) {
            publisherService.savePublishers(request.getId());
        } else {
            publisherService.removePublisherRequests(request.getId());
        }
        String message = "User " + request.getId() + " " + (request.isAction() ? "approved" : "not approved") + " successfully";
        return ResponseEntity.ok(new ApprovalResponse(message));
    }
}
