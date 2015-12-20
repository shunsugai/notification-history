package com.sugaishun.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class SbnUtil {

    private static final String TAG = SbnUtil.class.getSimpleName();

    private SbnUtil() {}

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static CharSequence getExtraTitle(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static CharSequence getExtraText(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getIcon(StatusBarNotification sbn) {
        return sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
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
