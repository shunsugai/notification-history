package net.ninterest.notification.model;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.view.ViewGroup;

import net.ninterest.notification.R;

public class NotificationItem {

    private static final String TAG = NotificationItem.class.getSimpleName();

    private StatusBarNotification mSbn;
    private Notification mNotif;
    private String mTitle;
    private String mText;
    private String mPackageName;
    private Context mContext;
    private SharedPreferences mSharedPrefs;

    public NotificationItem(Context context, StatusBarNotification sbn) {
        mSbn = sbn;
        mNotif = sbn.getNotification();
        mTitle = (String) mNotif.extras.getCharSequence(Notification.EXTRA_TITLE);
        mText = (String) mNotif.extras.getCharSequence(Notification.EXTRA_TEXT);
        mPackageName = mSbn.getPackageName();
        mContext = context;
        mSharedPrefs = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
    }

    /**
     * Validates StatusBarNotification.
     * @return {@code true} valid.
     */
    public boolean isValid() {
        boolean isValid = true;
        if (mSbn.isOngoing()) {
            isValid = false;
        }
        boolean isExcluded = mSharedPrefs.getBoolean(mPackageName, false);
        if (isExcluded) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Executes notification's action.
     * @return {@code true} execution succeeded.
     */
    public boolean execContentIntent() {
        PendingIntent pendingIntent = mNotif.contentIntent;
        if (pendingIntent == null) {
            return false;
        }
        Intent intent = new Intent();
        try {
            pendingIntent.send(mContext, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns notification's content view.
     * Returns big content view if the notification has it.
     * @param parent
     * @return
     */
    public View getContentView(ViewGroup parent) {
        View notifView;
        if (mNotif.bigContentView == null) {
            notifView = mNotif.contentView.apply(mContext, parent);
        } else {
            notifView = mNotif.bigContentView.apply(mContext, parent);
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
