package net.ninterest.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.ninterest.notification.analysis.Mixpanel;

public abstract class BaseActivity extends AppCompatActivity {

    protected Mixpanel mMixpanel;

    protected Tracker mTracker;

    protected abstract String getScreenName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMixpanel();
        initGoogleAnalytics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(getScreenName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        mMixpanel.flush();
        super.onDestroy();
    }

    private void initMixpanel() {
        mMixpanel = new Mixpanel(this);
        mMixpanel.trackLaunch(getScreenName());
    }

    private void initGoogleAnalytics() {
        ThisApplication application = (ThisApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }
}
