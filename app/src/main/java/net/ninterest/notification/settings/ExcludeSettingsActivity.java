package net.ninterest.notification.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import net.ninterest.notification.BaseActivity;
import net.ninterest.notification.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExcludeSettingsActivity extends BaseActivity {

    @Override
    protected String getScreenName() {
        return ExcludeSettingsActivity.class.getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclude_settings);

        initToolBar();

        final SharedPreferences preferences = getSharedPreferences(
                getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);

        ListView listView = (ListView) findViewById(R.id.exclude_apps_list);

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        Collections.sort(packages, new ApplicationInfoComparator(this));

        final BaseAdapter adapter = new ExcludeSettingsAdapter(this, packages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.exclude_settings_checkbox);
                ApplicationInfo appInfo = (ApplicationInfo) adapter.getItem(position);
                String packageName = appInfo.packageName;

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(packageName, !cb.isChecked());
                editor.apply();

                cb.setChecked(!cb.isChecked());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private static class ApplicationInfoComparator implements Comparator<ApplicationInfo> {

        private PackageManager pm;

        public ApplicationInfoComparator(Context context) {
            pm = context.getPackageManager();
        }

        @Override
        public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
            String lAppLabel = lhs.loadLabel(pm).toString();
            String rAppLabel = rhs.loadLabel(pm).toString();
            return lAppLabel.toUpperCase().compareTo(rAppLabel.toUpperCase());
        }
    }
}
