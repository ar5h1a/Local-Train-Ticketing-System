package com.localtrain.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.localtrain.notification.entity.Notification;

public interface NotificationRepository 
        extends MongoRepository<Notification, String> {
}
