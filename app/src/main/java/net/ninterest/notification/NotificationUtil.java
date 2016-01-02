package net.ninterest.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import java.lang.reflect.Field;

public class NotificationUtil {

    private static final String TAG = NotificationUtil.class.getSimpleName();

    private NotificationUtil() {
    }

    private static int notificationId;

    public static void sendNotification(Context context) {
        Intent resultIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(android.R.drawable.ic_menu_more)
                .setContentTitle("My notification")
                .setContentText(notificationId + " Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!" + notificationId);
        NotificationManager m =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        m.notify(notificationId, builder.build());
        notificationId++;
    }

    public static void sendNotificationNoAction(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_more)
                .setContentTitle("This notification has no action")
                .setContentText("test test test");
        NotificationManager m =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        m.notify(notificationId, builder.build());
        notificationId++;
    }

    public static Intent createNotificationListenerSettingsIntent() {
        String action;
        final Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
        } else {
            Settings settings = new Settings();
            try {
                Field field = settings.getClass().getDeclaredField("ACTION_NOTIFICATION_LISTENER_SETTINGS");
                action = (String) field.get(settings);
            } catch (NoSuchFieldException
                    | IllegalAccessException
                    | ClassCastException e) {
                action = "android.settings.NOTIFICATION_LISTENER_SETTINGS";
            }
        }
        intent.setAction(action);
        return intent;
    }

    /**
     *
     * @param context
     * @return
     */
    public static StatusBarNotification createWelcomeNotification(Context context) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        createNotificationListenerSettingsIntent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.welcome))
                .setContentText(context.getString(R.string.setup))
                .setContentIntent(resultPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.setup)))
                .addAction(R.drawable.ic_settings_black_36dp,
                        context.getString(R.string.click_to_enable),
                        resultPendingIntent)
                .build();
        String packageName = context.getPackageName();
        int id = 1;
        String tag = packageName;
        UserHandle userHandle = android.os.Process.myUserHandle();
        int uid = android.os.Process.myUid();
        int pid = android.os.Process.myPid();
        int score = 0;
        long postTime = System.currentTimeMillis();

        return new StatusBarNotification(packageName, packageName, id, tag, uid, pid, score,
                notification, userHandle, postTime);
    }
}
