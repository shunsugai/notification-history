package com.sugaishun.notification;

import android.provider.BaseColumns;

public class PushNotification implements BaseColumns {
    public static String TABLE_NAME = "notification";

    public static String COLUMN_NAME_TITLE   = "title";
    public static String COLUMN_NAME_TEXT    = "text";
    public static String COLUMN_NAME_PACKAGE = "package";
    public static String COLUMN_NAME_INTENT  = "intent";
}
