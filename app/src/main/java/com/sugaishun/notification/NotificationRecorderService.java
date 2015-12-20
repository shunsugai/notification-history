package com.sugaishun.notification;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("OverrideAbstract")
public class NotificationRecorderService extends NotificationListenerService {
    private static final String TAG = NotificationRecorderService.class.getSimpleName();

    private static List<StatusBarNotification> sNotifications = new LinkedList<>();

    public static List<StatusBarNotification> getNotifications() {
        return sNotifications;
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
