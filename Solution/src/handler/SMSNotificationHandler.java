package handler;

import enums.NotificationType;
import model.Notification;
import model.User;

public class SMSNotificationHandler implements NotificationHandler {
    public void handleNotification(User user, Notification notification) {
        System.out.println("Sending [SMS] notification: " + notification.getMessage() +" to User: " + user.getContactNumber());
    }
    @Override
    public NotificationType getType() {
        return NotificationType.SMS;
    }
}
