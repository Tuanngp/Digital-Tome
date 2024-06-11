package com.fpt.swp391.group6.DigitalTome.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRest {

    private final SimpMessagingTemplate template;

    @Autowired
    public NotificationRest(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody String message) {
        this.template.convertAndSend("/comment/notification", message);
        return ResponseEntity.ok().build();
    }
}