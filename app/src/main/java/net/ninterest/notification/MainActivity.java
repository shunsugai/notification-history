package net.ninterest.notification;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.ninterest.notification.model.NotificationItem;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ACTION_UPDATE = "net.ninterest.notification.ACTION_UPDATE";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_UPDATE.equals(action)) {
                mAdapter.notifyItemInserted(0);
            }
        }
    };

    private List<NotificationItem> mNotifications;

    private NotificationAdapter mAdapter;

    private CoordinatorLayout mCoordinatorLayout;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        NotificationItemManager m = NotificationItemManager.getInstance();
        mNotifications = m.getNotificatonItems();
        mAdapter = new NotificationAdapter(this, mNotifications);

        initToolbar();
        initRecyclerView();
        initFloatingActionButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        IntentFilter filter = new IntentFilter(ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        StatusBarNotification sbn = NotificationUtil.createWelcomeNotification(this);
        NotificationItem notificationItem = new NotificationItem(sbn);

        if (NotificationRecorderService.isEnabled(this)) {
            int position = mNotifications.indexOf(notificationItem);
            if (position >= 0) {
                mNotifications.remove(position);
            }
        } else {
            NotificationItemManager manager = NotificationItemManager.getInstance();
            manager.addFirst(notificationItem);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_debug, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) {
            switch (item.getItemId()) {
                case R.id.action_debug_issue_notification:
                    NotificationUtil.sendNotification(this);
                    return true;
                case R.id.action_debug_issue_notification_no_action:
                    NotificationUtil.sendNotificationNoAction(this);
                    return true;
                case R.id.action_debug_move_to_settings:
                    Intent intent = NotificationUtil.createNotificationListenerSettingsIntent();
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.e(TAG, "Matched activity not exist.");
                    }
                    return true;
            }
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

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemDecor = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        mAdapter.notifyItemMoved(fromPos, toPos);
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final NotificationItem backup = mNotifications.get(fromPos);
                        mNotifications.remove(fromPos);
                        mAdapter.notifyItemRemoved(fromPos);
                        Snackbar.make(mCoordinatorLayout,
                                R.string.msg_one_item_deleted, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.action_undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mNotifications.add(fromPos, backup);
                                        mAdapter.notifyItemInserted(fromPos);
                                    }
                                })
                                .show();
                    }
                });
        itemDecor.attachToRecyclerView(recyclerView);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNotifications.size() == 0) {
                    return;
                }
                final List<NotificationItem> items = new ArrayList<>(mNotifications);
                mAdapter.clearData();
                Snackbar.make(mCoordinatorLayout, R.string.msg_all_items_deleted, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.action_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mNotifications.addAll(items);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
    }
}
