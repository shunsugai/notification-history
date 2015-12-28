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
    private String mTitle;
    private String mText;
    private String mPackageName;

    public NotificationItem(StatusBarNotification sbn) {
        mSbn = sbn;
        mNotif = sbn.getNotification();
        mTitle = (String) mNotif.extras.getCharSequence(Notification.EXTRA_TITLE);
        mText = (String) mNotif.extras.getCharSequence(Notification.EXTRA_TEXT);
        mPackageName = mSbn.getPackageName();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationItem that = (NotificationItem) o;

        if (!mTitle.equals(that.mTitle)) return false;
        if (!mText.equals(that.mText)) return false;
        return mPackageName.equals(that.mPackageName);

    }

    @Override
    public int hashCode() {
        int result = mTitle.hashCode();
        result = 31 * result + mText.hashCode();
        result = 31 * result + mPackageName.hashCode();
        return result;
    }
}
