package net.ninterest.notification;

import android.app.Application;

import com.helpshift.Core;
import com.helpshift.support.Support;

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initHelpShift();
    }

    private void initHelpShift() {
        Core.init(Support.getInstance());
        Core.install(this,
                getString(R.string.helpshift_api_key),
                getString(R.string.helpshift_domein),
                getString(R.string.helpshift_app_id));
    }
}
