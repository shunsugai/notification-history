package com.sugaishun.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressLint("OverrideAbstract")
public class NotificationRecorderService extends NotificationListenerService {
    private static final String TAG = NotificationRecorderService.class.getSimpleName();

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private static List<StatusBarNotification> sNotifications = new LinkedList<>();

    public static List<StatusBarNotification> getNotifications() {
        return sNotifications;
    }

    /**
     * Returns whether this NotificationListenerService is enabled or not.
     * @param c context
     * @return {@code true} enabled. {@code false} disabled.
     */
    public static boolean isEnabled(Context c) {
        Objects.requireNonNull(c, "context cannot be null");
        String enabledServices =
                Settings.Secure.getString(c.getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        String thisService = c.getPackageName() + "/" + NotificationRecorderService.class.getName();
        return enabledServices != null && enabledServices.contains(thisService);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationPosted() called");
        Log.d(TAG, "sbn: " + sbn.toString());
        if (isValid(sbn)) {
            sNotifications.add(0, sbn);
            sendBroadcast(MainActivity.createIntent());
        }
    }

    private boolean isValid(StatusBarNotification sbn) {
        boolean result = true;
        if (sbn.isOngoing()) {
            result = false;
        }
        return result;
    }
}
