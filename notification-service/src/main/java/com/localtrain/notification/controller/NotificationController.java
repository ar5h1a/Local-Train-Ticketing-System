package com.localtrain.notification.controller;

import org.springframework.web.bind.annotation.*;
import com.localtrain.notification.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public String sendNotification(
            @RequestParam Long userId,
            @RequestParam String type) {

        notificationService.sendNotification(userId, type);

        return "Notification request accepted";
    }
}
