package com.sugaishun.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.service.notification.StatusBarNotification;

public class SbnUtil {
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
}
