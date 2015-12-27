package net.ninterest.notification.model;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class NotificationItem {

    private static final String TAG = NotificationItem.class.getSimpleName();

    private StatusBarNotification mSbn;
    private Notification mNotif;

    public NotificationItem(StatusBarNotification sbn) {
        mSbn = sbn;
        mNotif = sbn.getNotification();
    }

    /**
     * Validates StatusBarNotification.
     * @return {@code true} valid.
     */
    public boolean isValid() {
        boolean result = true;
        if (mSbn.isOngoing()) {
            result = false;
        }
        return result;
    }

    /**
     * Executes notification's action.
     * @param context
     * @return {@code true} execution succeeded.
     */
    public boolean execContentIntent(Context context) {
        Objects.requireNonNull(context, "context cannot be null");
        PendingIntent pendingIntent = mNotif.contentIntent;
        if (pendingIntent == null) {
            Log.w(TAG, "pending intent is not set");
            return false;
        }
        Intent intent = new Intent();
        try {
            pendingIntent.send(context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Log.w(TAG, "failed", e);
            return false;
        }
        return true;
    }

    /**
     * Returns notification's content view.
     * Returns big content view if the notification has it.
     * @param context
     * @param parent
     * @return
     */
    public View getContentView(Context context, ViewGroup parent) {
        Objects.requireNonNull(context, "context cannot be null");
        View notifView;
        if (mNotif.bigContentView == null) {
            notifView = mNotif.contentView.apply(context, parent);
        } else {
            notifView = mNotif.bigContentView.apply(context, parent);
        }
        return notifView;
    }
}
