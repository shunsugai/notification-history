package com.sugaishun.notification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationRecorderService extends NotificationListenerService {
    private static final String TAG = NotificationRecorderService.class.getSimpleName();

    private static List<StatusBarNotification> sNotifications = new LinkedList<>();

    public static List<StatusBarNotification> getNotifications() {
        return sNotifications;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
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
