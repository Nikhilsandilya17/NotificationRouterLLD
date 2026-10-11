package handler;

import enums.NotificationType;
import model.Notification;
import model.User;

public interface NotificationHandler {
    void handleNotification(User user, Notification notification);

    NotificationType getType();
}
