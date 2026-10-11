package model;

import enums.NotificationType;

import java.util.*;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String contactNumber;
    private final Set<NotificationType> notificationPreferences;

    public User(String name, String email, String contactNumber) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.notificationPreferences = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public synchronized Set<NotificationType> getNotificationPreferences() {
        return Set.copyOf(notificationPreferences);
    }

    public synchronized void addNotificationPreference(NotificationType notificationType) {
        this.notificationPreferences.add(notificationType);
    }

    public synchronized void optOutOfNotificationPreference(NotificationType notificationType){
        this.notificationPreferences.remove(notificationType);
    }


}
