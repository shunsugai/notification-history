package net.ninterest.notification;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.helpshift.Core;
import com.helpshift.support.Support;

public class ThisApplication extends Application {

    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        initHelpShift();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    private void initHelpShift() {
        Core.init(Support.getInstance());
        Core.install(this,
                getString(R.string.helpshift_api_key),
                getString(R.string.helpshift_domain),
                getString(R.string.helpshift_app_id));
    }
}
