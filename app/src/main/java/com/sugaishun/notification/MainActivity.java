package com.sugaishun.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

import de.timroes.android.listview.EnhancedListView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ACTION_UPDATE = "com.sugaishun.notification.ACTION_UPDATE";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    private BaseAdapter adapter;
    private List<StatusBarNotification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        EnhancedListView listView = (EnhancedListView) findViewById(R.id.listView);
        notifications = NotificationRecorderService.getNotifications();
        adapter = new NotificationListAdapter(getApplicationContext(), notifications);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StatusBarNotification sbn = notifications.get(position);
                SbnUtil.sendNotificationIntent(MainActivity.this, sbn);
            }
        });
        listView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int position) {
                notifications.remove(position);
                return null;
            }
        });
        listView.enableSwipeToDismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        adapter.notifyDataSetChanged();
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.action_bar_title);
        setSupportActionBar(toolbar);
    }
}
