package com.sugaishun.notification;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "main.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PushNotification.TABLE_NAME + " ( " +
            PushNotification._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PushNotification.COLUMN_NAME_TITLE   + " TEXT, " +
            PushNotification.COLUMN_NAME_TEXT    + " TEXT, " +
            PushNotification.COLUMN_NAME_PACKAGE + " TEXT, " +
            PushNotification.COLUMN_NAME_INTENT  + " BLOB" +
            " )";

    public NotificationOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
