package com.sugaishun.notification;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
        holder.frameLayout.addView(
                items.get(position).getNotification().contentView.apply(context, parent));
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position" + position);
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
