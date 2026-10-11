package router;

import enums.NotificationPriority;
import enums.NotificationType;
import handler.NotificationHandler;
import model.Notification;
import model.User;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationServiceImpl implements NotificationService {
    private final Map<NotificationType, NotificationHandler> handlerRegistry;

    public NotificationServiceImpl(List<NotificationHandler> notificationHandlers) {
        this.handlerRegistry = new HashMap<>();
        for (NotificationHandler handler : notificationHandlers) {
            this.handlerRegistry.put(handler.getType(), handler);
        }
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        if (notification.getNotificationPriority() == NotificationPriority.URGENT) {
            for (NotificationHandler handler : handlerRegistry.values()) {
                handler.handleNotification(user, notification);
            }
        } else {
            for (NotificationType notificationType : user.getNotificationPreferences()) {
                NotificationHandler handler = handlerRegistry.get(notificationType);
                if (handler != null) {
                    handler.handleNotification(user, notification);
                }
            }
        }
    }

    @Override
    public Notification createNotification(String message, String sentTo, NotificationPriority notificationPriority) {
        return new Notification(message, sentTo, notificationPriority);
    }
}
