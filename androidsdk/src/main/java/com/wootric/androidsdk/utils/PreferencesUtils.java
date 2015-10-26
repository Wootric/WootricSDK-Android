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

    private static final long DAY_IN_MILLIS = 1000L *60L *60L *24L;
    private static final long NOT_SET = -1;

    private final WeakReference<Context> weakContext;

    public PreferencesUtils(WeakReference<Context> weakContext) {
        if(weakContext == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

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
        if(prefs != null) {
            long eventTime = prefs.getLong(key, NOT_SET);
            recentTime = (eventTime != -1 && new Date().getTime() - eventTime < DAY_IN_MILLIS * 90L);
        }

        return recentTime;
    }
}
