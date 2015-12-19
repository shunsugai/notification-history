package com.sugaishun.notification;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotificationListAdapter extends BaseAdapter {

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView text;

        public ViewHolder(View v) {
            icon = (ImageView) v.findViewById(R.id.listItemIcon);
            title = (TextView) v.findViewById(R.id.listItemTitle);
            text = (TextView) v.findViewById(R.id.listItemMessage);
        }
    }

    private Context context;
    private List<StatusBarNotification> items;

    public NotificationListAdapter(Context context, List<StatusBarNotification> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatusBarNotification sbn = (StatusBarNotification) getItem(position);
        Drawable appIcon = null;
        try {
            appIcon = context.getPackageManager().getApplicationIcon(sbn.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
        }
        holder.icon.setImageDrawable(appIcon);
        holder.title.setText(SbnUtil.getExtraTitle(sbn));
        holder.text.setText(SbnUtil.getExtraText(sbn));
        return convertView;
    }
}
