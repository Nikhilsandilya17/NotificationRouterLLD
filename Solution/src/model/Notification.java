package model;

import enums.NotificationPriority;
import enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private final String id;
    private final LocalDateTime createdAt;
    private final String message;
    private final String sentTo;
    private final NotificationPriority notificationPriority;

    public Notification(String message, String sentTo, NotificationPriority notificationPriority) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.message = message;
        this.sentTo = sentTo;
        this.notificationPriority = notificationPriority;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public String getSentTo() {
        return sentTo;
    }
    public NotificationPriority getNotificationPriority() {
        return notificationPriority;
    }

}
