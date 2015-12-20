package com.sugaishun.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SbnUtil {

    private static final String TAG = SbnUtil.class.getSimpleName();

    private SbnUtil() {
    }

    public static CharSequence getExtraTitle(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE);
    }

    public static CharSequence getExtraText(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
    }

    public static void sendNotificationIntent(Context context, StatusBarNotification sbn) {
        PendingIntent pendingIntent = sbn.getNotification().contentIntent;
        if (pendingIntent == null) {
            Log.d(TAG, "pending intent is not set");
            return;
        }
        Intent intent = new Intent();
        try {
            pendingIntent.send(context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Log.w(TAG, "failed", e);
        }
    }
}
