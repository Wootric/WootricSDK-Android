package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class PreferencesUtils {

    private static final String KEY_PREFERENCES = "com.wootric.androidsdk.prefs";
    private static final String KEY_LAST_SEEN = "last_seen";
    private static final String KEY_LAST_SURVEYED = "surveyed";

    private static final long DAY_IN_MILLIS = 1000L *60L *60L *24L;
    private static final long NOT_SET = -1;

    private final Context context;

    public PreferencesUtils(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

        this.context = context;
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void clear() {
        prefs().edit().clear().apply();
    }

    public void touchLastSurveyed() {
        prefs().edit().putLong(KEY_LAST_SURVEYED, new Date().getTime()).apply();
    }

    public boolean wasRecentlySurveyed() {
        return isRecentTime(KEY_LAST_SURVEYED);
    }

    public void touchLastSeen() {
        if (!wasRecentlySeen()) {
            prefs().edit().putLong(KEY_LAST_SEEN, new Date().getTime()).apply();
        }
    }

    public long getLastSeen() {
        return prefs().getLong(KEY_LAST_SEEN, NOT_SET);
    }

    public boolean isLastSeenSet() {
        return getLastSeen() != NOT_SET;
    }

    private boolean wasRecentlySeen() {
        return isRecentTime(KEY_LAST_SEEN);
    }

    private boolean isRecentTime(String key) {
        long eventTime = prefs().getLong(key, NOT_SET);

        return eventTime != -1 && new Date().getTime() - eventTime < DAY_IN_MILLIS * 90L;

    }
}
