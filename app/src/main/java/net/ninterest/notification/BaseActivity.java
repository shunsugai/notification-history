package net.ninterest.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.ninterest.notification.analysis.Mixpanel;

public abstract class BaseActivity extends AppCompatActivity {

    protected Mixpanel mMixpanel;

    protected abstract String getScreenName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMixpanel = new Mixpanel(this);
        mMixpanel.trackLaunch(getScreenName());
    }

    @Override
    protected void onDestroy() {
        mMixpanel.flush();
        super.onDestroy();
    }
}
