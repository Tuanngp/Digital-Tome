package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.dto.appRegister.ApprovalRequest;
import com.fpt.swp391.group6.DigitalTome.dto.appRegister.ApprovalResponse;
import com.fpt.swp391.group6.DigitalTome.service.EmailService;
import com.fpt.swp391.group6.DigitalTome.service.PublisherService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublisherController {
    private final PublisherService publisherService;


    public PublisherController(PublisherService publisherService, EmailService emailService, UserService userService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/approve")
    public ResponseEntity<ApprovalResponse> approveUser(@RequestBody ApprovalRequest request) {
<<<<<<< HEAD
        System.out.println("User ID: " + request.getUserId());
        System.out.println("Action: " + request.getAction());

        if (request.getAction().equalsIgnoreCase("Approve")) {
            publisherService.savePublishers(request.getUserId());
        } else {
            publisherService.removePublisherRequests(request.getUserId());
        }
        String message = "User " + (request.getAction().equalsIgnoreCase("Approve") ? "approved" : "not approved") + " successfully";
=======
        if (request.isAction()) {
            publisherService.savePublishers(request.getId());
        } else {
            publisherService.removePublisherRequests(request.getId());
        }
        String message = "User " + request.getId() + " " + (request.isAction() ? "approved" : "not approved") + " successfully";
>>>>>>> origin/khanhduc-workspace
        return ResponseEntity.ok(new ApprovalResponse(message));
    }
}