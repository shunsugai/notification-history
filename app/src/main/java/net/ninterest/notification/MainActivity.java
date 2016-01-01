package net.ninterest.notification;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.animation.AlphaAnimation;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.ninterest.notification.model.NotificationItem;
import net.ninterest.notification.settings.SettingsActivity;

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

    private RecyclerView.AdapterDataObserver mEmptyObserver;

    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        initAdView();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mEmptyView = this.findViewById(R.id.textview_no_notifications);

        NotificationItemManager m = NotificationItemManager.getInstance();
        mNotifications = m.getNotificatonItems();
        mAdapter = new NotificationAdapter(this, mNotifications);

        registerObserver();
        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        showMessageIfNotificationServiceDisabled();

        IntentFilter filter = new IntentFilter(ACTION_UPDATE);
        registerReceiver(mReceiver, filter);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mEmptyObserver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_debug, menu);
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_clear_all:
                clearAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent createIntent() {
        return new Intent(MainActivity.ACTION_UPDATE);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                        // do nothing
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

    private void registerObserver() {
        mEmptyObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfEmpty();
            }
        };
        mAdapter.registerAdapterDataObserver(mEmptyObserver);
    }

    private void checkIfEmpty() {
        if (mAdapter.getItemCount() == 0) {
            AlphaAnimation anim = new AlphaAnimation(0, 1);
            anim.setDuration(200);
            mEmptyView.startAnimation(anim);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void initAdView() {
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void showMessageIfNotificationServiceDisabled() {
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
    }

    private void clearAll() {
        if (mNotifications.size() == 0) {
            return;
        }
        final List<NotificationItem> backups = new ArrayList<>(mNotifications);
        mAdapter.clearData();
        Snackbar.make(mCoordinatorLayout, R.string.msg_all_items_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNotifications.addAll(backups);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .show();
    }
}
