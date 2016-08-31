/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.wootric.androidsdk.Constants;

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
    private static final String KEY_TYPE = "type";
    private static final String KEY_RESURVEY_DAYS = "resurvey_days";

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

    public void touchLastSurveyed(boolean responseSent, Integer resurvey_days) {
        final SharedPreferences prefs = prefs();
        if(prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        if(editor == null) return;

        editor.putLong(KEY_LAST_SURVEYED, new Date().getTime());
        if (responseSent) {
            editor.putString(KEY_TYPE, "response");
        } else {
            editor.putString(KEY_TYPE, "decline");
        }
        editor.putInt(KEY_RESURVEY_DAYS, resurvey_days);
        editor.apply();
    }

    public boolean wasRecentlySurveyed() {
        String type;
        Integer days = 90;
        final SharedPreferences prefs = prefs();
        if(prefs != null) {
            type = prefs.getString(KEY_TYPE, "");
            Integer resurvey_days = prefs.getInt(KEY_RESURVEY_DAYS, -1);
            if (resurvey_days >= 0){
                days = resurvey_days;
            } else {
                if (type.equals("decline")){
                    days = (int) Constants.DEFAULT_DECLINE_RESURVEY_DAYS;
                } else {
                    days = (int) Constants.DEFAULT_RESURVEY_DAYS;
                }

            }
        }

        return isRecentTime(KEY_LAST_SURVEYED, days);
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
        long lastSeen = Constants.NOT_SET;

        final SharedPreferences prefs = prefs();
        if(prefs != null) {
            lastSeen = prefs.getLong(KEY_LAST_SEEN, Constants.NOT_SET);
        }

        return lastSeen;
    }

    public boolean isLastSeenSet() {
        return getLastSeen() != Constants.NOT_SET;
    }

    private boolean wasRecentlySeen() {
        return isRecentTime(KEY_LAST_SEEN, 90L);
    }

    private boolean isRecentTime(String key, long days) {
        boolean recentTime = false;

        final SharedPreferences prefs = prefs();
        if (prefs != null) {
            long eventTime = prefs.getLong(key, Constants.NOT_SET);
            long delta = new Date().getTime() - eventTime;
            recentTime = (eventTime != -1 && delta < Constants.DAY_IN_MILLIS * days);
            if (recentTime){
                int daysAgo = (int) delta / (1000*60*60*24);
                Log.d(Constants.TAG, String.format("%s(expiration date %d days): %d days ago",
                        key,
                        days,
                        daysAgo));
            }
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
