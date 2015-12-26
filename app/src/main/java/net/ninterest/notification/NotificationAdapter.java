package net.ninterest.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = NotificationAdapter.class.getSimpleName();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout frameLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.notification_content);
        }
    }

    private Context context;
    private List<StatusBarNotification> items;

    public NotificationAdapter(Context context, List<StatusBarNotification> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ViewGroup parent = (ViewGroup) holder.frameLayout.getParent();
        holder.frameLayout.removeAllViews();
        final Notification notif = items.get(position).getNotification();
        View notifView;
        if (notif.bigContentView == null) {
            notifView = notif.contentView.apply(context, parent);
        } else {
            notifView = notif.bigContentView.apply(context, parent);
        }
        holder.frameLayout.addView(notifView);
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if (notif.contentIntent == null) {
                    Toast.makeText(
                            context, R.string.msg_no_action_associated, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    notif.contentIntent.send(context, 0, i);
                } catch (PendingIntent.CanceledException e) {
                    Toast.makeText(
                            context, R.string.msg_no_action_associated, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clearData() {
        int size = items.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                items.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}
