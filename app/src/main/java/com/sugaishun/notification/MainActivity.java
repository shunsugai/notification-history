package com.sugaishun.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ACTION_UPDATE = "com.sugaishun.notification.ACTION_UPDATE";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                mAadpter.notifyDataSetChanged();
            }
        }
    };

    private BaseAdapter mAadpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        final List<StatusBarNotification> notifications = NotificationRecorderService.getNotifications();
        mAadpter = new NotificationListAdapter(getApplicationContext(), notifications);
        listView.setAdapter(mAadpter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatusBarNotification sbn = notifications.get(position);
                SbnUtil.sendNotificationIntent(MainActivity.this, sbn);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        mAadpter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            NotificationUtil.sendNotification(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent() {
        return new Intent(MainActivity.ACTION_UPDATE);
    }
}
