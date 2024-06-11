package com.fpt.swp391.group6.DigitalTome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate template;

    @Autowired
    public NotificationController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendNotification(String message) {
        this.template.convertAndSend("/topic/notification", message);
    }
}
