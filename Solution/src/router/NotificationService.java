package router;

import enums.NotificationPriority;
import model.Notification;
import model.User;

public interface NotificationService {
    void sendNotification(User user, Notification notification);
    Notification createNotification(String message, String sentTo, NotificationPriority notificationPriority);
}
