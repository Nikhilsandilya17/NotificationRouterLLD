package handler;

import enums.NotificationType;
import model.Notification;
import model.User;

public class EmailNotificationHandler implements NotificationHandler {

    public void handleNotification(User user, Notification notification) {
        System.out.println("Sending [EMAIL] notification: " + notification.getMessage() +" to User: " + user.getEmail());
    }

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }
}
