import enums.NotificationPriority;
import enums.NotificationType;
import handler.EmailNotificationHandler;
import handler.NotificationHandler;
import handler.PushNotificationHandler;
import handler.SMSNotificationHandler;
import model.Notification;
import model.User;
import router.NotificationService;
import router.NotificationServiceImpl;

import java.util.List;

public class NotificationRouterDemo {
    static void main(String[] args) {
        System.out.println("NotificationRouterDemo");
        List<NotificationHandler> handlers = List.of(
                new EmailNotificationHandler(),
                new SMSNotificationHandler(),
                new PushNotificationHandler());
        NotificationService notificationService = new NotificationServiceImpl(handlers);

        User user = new User("Nikhil", "nikhil@gmail.com", "123456789");
        user.addNotificationPreference(NotificationType.EMAIL);
        user.addNotificationPreference(NotificationType.SMS);
        Notification urgentNotification = notificationService.createNotification("Your order is delivered", user.getId(), NotificationPriority.URGENT);
        Notification normalNotification = notificationService.createNotification("Your order has arrived", user.getId(), NotificationPriority.NORMAL);
        notificationService.sendNotification(user, urgentNotification);
        notificationService.sendNotification(user, normalNotification);

    }
}
