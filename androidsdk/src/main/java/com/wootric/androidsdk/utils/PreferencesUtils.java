package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class PreferencesUtils {

    private static final String KEY_PREFERENCES = "com.wootric.androidsdk.prefs";
    private static final String KEY_LAST_SEEN = "last_seen";
    private static final String KEY_LAST_SURVEYED = "surveyed";
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_DECLINE = "decline";

    private static final long DAY_IN_MILLIS = 1000L *60L *60L *24L;
    private static final long NOT_SET = -1;

    private final WeakReference<Context> weakContext;

    public PreferencesUtils(WeakReference<Context> weakContext) {
        this.weakContext = weakContext;
    }

    private SharedPreferences prefs() {
        final Context context = weakContext.get();

        if(context != null) {
            return context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE);
        }

        return null;
    }

    public void touchLastSurveyed() {
        final SharedPreferences prefs = prefs();
        if(prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        if(editor == null) return;

        editor.putLong(KEY_LAST_SURVEYED, new Date().getTime()).apply();
    }

    public boolean wasRecentlySurveyed() {
        return isRecentTime(KEY_LAST_SURVEYED);
    }

    public void touchLastSeen() {
        if(wasRecentlySeen()) return;

        final SharedPreferences prefs = prefs();
        if(prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        if(editor == null) return;


        editor.putLong(KEY_LAST_SEEN, new Date().getTime()).apply();
    }

    public long getLastSeen() {
        long lastSeen = NOT_SET;

        final SharedPreferences prefs = prefs();
        if(prefs != null) {
            lastSeen = prefs.getLong(KEY_LAST_SEEN, NOT_SET);
        }

        return lastSeen;
    }

    public boolean isLastSeenSet() {
        return getLastSeen() != NOT_SET;
    }

    private boolean wasRecentlySeen() {
        return isRecentTime(KEY_LAST_SEEN);
    }

    private boolean isRecentTime(String key) {
        boolean recentTime = false;

        final SharedPreferences prefs = prefs();
        if (prefs != null) {
            long eventTime = prefs.getLong(key, NOT_SET);
            recentTime = (eventTime != -1 && new Date().getTime() - eventTime < DAY_IN_MILLIS * 90L);
        }

        return recentTime;
    }

    public void putResponse(String response) {
        putString(KEY_RESPONSE, response);
    }

    public String getResponse() {
        return getString(KEY_RESPONSE);
    }

    public void putDecline(String decline) {
        putString(KEY_DECLINE, decline);
    }

    public String getDecline() {
        return getString(KEY_DECLINE);
    }

    private void putString(String key, String value) {
        final SharedPreferences prefs = prefs();
        if(prefs != null) {
            prefs.edit().putString(key, value).apply();
        }
    }

    private String getString(String key) {
        String value = null;

        final SharedPreferences prefs = prefs();
        if(prefs != null) {
            value = prefs.getString(key, null);
        }

        return value;
    }
}
