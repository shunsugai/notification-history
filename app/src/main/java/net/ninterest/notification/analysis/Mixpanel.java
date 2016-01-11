package net.ninterest.notification.analysis;

import android.content.Context;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import net.ninterest.notification.BuildConfig;
import net.ninterest.notification.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Mixpanel {

    private static final String EVENT_LAUNCH = "net.ninterest.notification.EVENT_LAUNCH";

    private static final String EVENT_MENU = "net.ninterest.notification.EVENT_MENU";

    private static final String TAG = Mixpanel.class.getSimpleName();

    private MixpanelAPI mMixpanelApi;

    public Mixpanel(Context context) {
        mMixpanelApi = MixpanelAPI.getInstance(context,
                context.getString(R.string.mixpanel_api_token));
    }

    public void trackLaunch(String screenName) {
        if (BuildConfig.DEBUG) {
            return;
        }
        Objects.requireNonNull(screenName, "screenName cannot be null");
        JSONObject props = new JSONObject();
        try {
            props.put(EVENT_LAUNCH, screenName);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to add properties to JSONObject");
        }
        mMixpanelApi.track(EVENT_LAUNCH, props);
    }

    public void trackMenu(String menu) {
        if (BuildConfig.DEBUG) {
            return;
        }
        Objects.requireNonNull(menu, "menu cannot be null");
        JSONObject props = new JSONObject();
        try {
            props.put(EVENT_MENU, menu);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to add properties to JSONObject");
        }
        mMixpanelApi.track(EVENT_MENU, props);
    }

    public void flush() {
        mMixpanelApi.flush();
    }
}
