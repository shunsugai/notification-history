package net.ninterest.notification;

import net.ninterest.notification.model.NotificationItem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationItemManager {

    private static NotificationItemManager sNotificationManager;

    private List<NotificationItem> mNotifications = new CopyOnWriteArrayList<>();

    private NotificationItemManager() {
    }

    public static synchronized NotificationItemManager getInstance() {
        if (sNotificationManager == null) {
            sNotificationManager = new NotificationItemManager();
        }
        return sNotificationManager;
    }

    public List<NotificationItem> getNotificatonItems() {
        return mNotifications;
    }

    public boolean addFirst(NotificationItem notification) {
        if (!notification.isValid()) {
            return false;
        }
        if (mNotifications.contains(notification)) {
            return false;
        }
        mNotifications.add(0, notification);
        return true;
    }
}
