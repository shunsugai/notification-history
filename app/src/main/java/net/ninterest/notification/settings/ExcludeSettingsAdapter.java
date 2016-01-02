package net.ninterest.notification.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import net.ninterest.notification.R;

import java.util.List;

public class ExcludeSettingsAdapter extends BaseAdapter {

    private List<ApplicationInfo> mList;
    private Context mContext;
    private PackageManager mPackageManager;
    private SharedPreferences mSharedPrefs;

    public ExcludeSettingsAdapter(Context context, List<ApplicationInfo> list) {
        mList = list;
        mContext = context;
        mPackageManager = context.getPackageManager();
        mSharedPrefs = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.exclude_settings_row, parent, false);
        }

        final ApplicationInfo appInfo = (ApplicationInfo) getItem(position);

        ImageView iconView = (ImageView) convertView.findViewById(R.id.exclude_settings_app_icon);
        Drawable appIcon = null;
        try {
            appIcon = mPackageManager.getApplicationIcon(appInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: Set dummy icon
        }
        iconView.setImageDrawable(appIcon);

        TextView appNameView = (TextView) convertView.findViewById(R.id.exclude_settings_app_name);
        CharSequence appDisplayName = appInfo.loadLabel(mPackageManager);
        appNameView.setText(appDisplayName);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.exclude_settings_checkbox);
        boolean isChecked = mSharedPrefs.getBoolean(appInfo.packageName, false);
        checkBox.setChecked(isChecked);

        return convertView;
    }
}
