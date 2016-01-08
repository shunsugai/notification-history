package net.ninterest.notification;

import android.content.Context;
import android.content.SharedPreferences;

public class StatusBarCleaner {

    private static final String PREF_AUTO_DISMISS_ENABLED = "net.ninterest.notification.AUTO_DISMISS_ENABLED";

    private Context context;

    private SharedPreferences preferences;

    public StatusBarCleaner(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
    }

    public void start() {
        NotificationUtil.notifyCleanBarEnabled(context);
        preferences
                .edit()
                .putBoolean(PREF_AUTO_DISMISS_ENABLED, true)
                .apply();
    }

    public void stop() {
        NotificationUtil.cancelCleanBarNotification(context);
        preferences
                .edit()
                .putBoolean(PREF_AUTO_DISMISS_ENABLED, false)
                .apply();
    }

    public boolean isRunning() {
        return preferences.getBoolean(PREF_AUTO_DISMISS_ENABLED, false);
    }
}
