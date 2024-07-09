package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.entity.NotificationEntity;
import com.fpt.swp391.group6.DigitalTome.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/current")
    public ResponseEntity<List<NotificationEntity>> getCurrentUserNotifications() {
        List<NotificationEntity> notifications = notificationService.getNotificationsForCurrentUser();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
