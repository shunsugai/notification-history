package net.ninterest.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.ninterest.notification.model.NotificationItem;

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
    private List<NotificationItem> items;

    public NotificationAdapter(Context context, List<NotificationItem> items) {
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

        final NotificationItem notification = items.get(position);
        View notifView = notification.getContentView(context, parent);
        holder.frameLayout.addView(notifView);
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!notification.execContentIntent(context)) {
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
