package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.NotificationEntity;
import com.fpt.swp391.group6.DigitalTome.repository.NotificationRepository;
import com.fpt.swp391.group6.DigitalTome.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository  userRepository;


    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    public void save(NotificationEntity notification) {
        notificationRepository.save(notification);
    }

    // Lấy thông báo của người dùng hiện tại
    public List<NotificationEntity> getNotificationsForCurrentUser() {
        String username = getCurrentUsername();
        AccountEntity account = userRepository.findByUsername(username);
        if (account != null) {
            List<NotificationEntity> notifications = notificationRepository.findByUserIdOrderByIdDesc(account.getId());
            for (NotificationEntity notification : notifications) {
                assert username != null;
                messagingTemplate.convertAndSendToUser(username, "/queue/notifications", notification);
            }
            return notifications;
        }
        return List.of();
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
    }

    public void updateNotification(Long accountId, String message, String title, String url) {
        Optional<AccountEntity> accountOpt = userRepository.findById(accountId);
        if (accountOpt.isPresent()) {
            AccountEntity account = accountOpt.get();
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(account);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setIsRead(false);
            notification.setUrl(url);
            notification.setAvatarUrl(account.getAvatarPath());
            notificationRepository.save(notification);
            // Gửi thông báo
            messagingTemplate.convertAndSendToUser(account.getUsername(), "/queue/notifications", notification);
        }
    }

    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

}