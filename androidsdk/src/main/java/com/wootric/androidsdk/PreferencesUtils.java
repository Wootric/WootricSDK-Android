package com.wootric.androidsdk;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import static com.wootric.androidsdk.Constants.NOT_SET;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class PreferencesUtils {

    static final String KEY_PREFERENCES = "com.wootric.androidsdk.prefs";

    static final String KEY_LAST_SEEN = "last_seen";
    static final String KEY_LAST_SURVEYED = "surveyed";

    private static PreferencesUtils singleton = null;

    private final Context context;

    static PreferencesUtils getInstance(Context context) {
       if(singleton == null) {
           singleton = new PreferencesUtils(context);
       }

       return singleton;
    }

    private PreferencesUtils(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

        this.context = context;
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
    }

    boolean wasRecentlyLastSeen() {
        return isRecentTime(KEY_LAST_SEEN);
    }

    boolean wasRecentlySurveyed() {
        return isRecentTime(KEY_LAST_SURVEYED);
    }

    private boolean isRecentTime(String key) {
        long eventTime = prefs().getLong(key, NOT_SET);

        if(eventTime == NOT_SET) {
            return false;
        }

        long ninetyDays = 1000*60*60*24*90;
        return new Date().getTime() - eventTime < ninetyDays;
    }

    long getLastSeen() {
        return prefs().getLong(KEY_LAST_SEEN, NOT_SET);
    }

    boolean isLastSeenSet() {
        return prefs().getLong(KEY_LAST_SEEN, NOT_SET) != NOT_SET;
    }

    void setLastSeen(long lastSeen) {
        prefs().edit().putLong(KEY_LAST_SEEN, lastSeen).apply();
    }

    void setLastSurveyed(long lastSurveyed) {
        prefs().edit().putLong(KEY_LAST_SURVEYED, lastSurveyed).apply();
    }
}
