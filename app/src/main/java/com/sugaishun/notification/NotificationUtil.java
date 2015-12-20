package com.sugaishun.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
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
                .setContentTitle("My notification My notification My notification My notification My notification")
                .setContentText("Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!" + notificationId);
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
}
