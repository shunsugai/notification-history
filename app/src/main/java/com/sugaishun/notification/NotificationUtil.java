package com.sugaishun.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationUtil {

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
}
