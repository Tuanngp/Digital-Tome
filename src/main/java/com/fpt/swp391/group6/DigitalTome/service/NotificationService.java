package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.NotificationEntity;
import com.fpt.swp391.group6.DigitalTome.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final UserService userService;


    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate, UserService userService) {

        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    public void save(NotificationEntity notification) {
        notificationRepository.save(notification);
    }


    public List<NotificationEntity> getNotificationsForCurrentUser() {
        AccountEntity account = userService.getCurrentLogin();

        if (account != null) {
            String username = account.getUsername();
            List<NotificationEntity> notifications = notificationRepository.findByUserIdOrderByIdDesc(account.getId());
            for (NotificationEntity notification : notifications) {
                messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
            }
            return notifications;
        }
        return List.of();
    }


    public void createNotification(AccountEntity account, AccountEntity sender, String message, String title, String url) {

        NotificationEntity notification = new NotificationEntity();
        notification.setUser(account);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setUrl(url);
        notification.setAvatarUrl(sender.getAvatarPath());
        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(account.getUsername(), "/queue/notifications", notification);
    }

    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

}