package handler;

import enums.NotificationType;
import model.Notification;
import model.User;

public class PushNotificationHandler implements NotificationHandler {
    public void handleNotification(User user, Notification notification) {
        System.out.println("Sending [PUSH] notification: " + notification.getMessage() +" to User: " + user.getName());
    }
    @Override
    public NotificationType getType() {
        return NotificationType.PUSH;
    }
}
