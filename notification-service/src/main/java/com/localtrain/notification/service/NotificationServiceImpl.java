package com.localtrain.notification.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.localtrain.notification.entity.Notification;
import com.localtrain.notification.repository.NotificationRepository;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final EmailService emailService;

    public NotificationServiceImpl(NotificationRepository repository,
                                   EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Async
    @Override
    public void sendNotification(Long userId, String type) {

        String userEmail = "rankakajal7@gmail.com"; // TEMP for testing

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTimestamp(LocalDateTime.now());
        notification.setStatus("SENT");

        Notification saved = repository.save(notification);

        // ✅ SEND EMAIL
        emailService.sendTicketEmail(userEmail, userId.toString());

        System.out.println("Saved notification ID: " + saved.getId());
    }
}

