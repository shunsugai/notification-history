package net.ninterest.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import net.ninterest.notification.model.NotificationItem;

import java.util.Objects;

@SuppressLint("OverrideAbstract")
public class NotificationRecorderService extends NotificationListenerService {
    private static final String TAG = NotificationRecorderService.class.getSimpleName();

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private NotificationItemManager mItemManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mItemManager = NotificationItemManager.getInstance();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        NotificationItem notification = new NotificationItem(sbn);
        if (mItemManager.addFirst(notification)) {
            sendBroadcast(MainActivity.createIntent());
        }
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
}
