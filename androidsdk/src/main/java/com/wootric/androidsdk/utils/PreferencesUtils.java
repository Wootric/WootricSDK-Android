package com.wootric.androidsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import static com.wootric.androidsdk.utils.Constants.INVALID_ID;
import static com.wootric.androidsdk.utils.Constants.NOT_SET;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class PreferencesUtils {

    private static final String KEY_PREFERENCES = "com.wootric.androidsdk.prefs";
    private static final String KEY_LAST_SEEN = "last_seen";
    private static final String KEY_LAST_SURVEYED = "surveyed";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_END_USER_ID = "end_user_id";

    private static final String KEY_SELECTED_SCORE = "selected_score";
    private static final String KEY_FEEDBACK_INPUT_VALUE = "feedback_input_value";
    private static final String KEY_CURRENT_STATE = "current_state";
    private static final String KEY_RESPONSE_SENT = "response_sent";

    private static PreferencesUtils singleton = null;

    private final Context context;

    public static PreferencesUtils getInstance(Context context) {
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

    public boolean wasRecentlySeen() {
        return isRecentTime(KEY_LAST_SEEN);
    }

    public boolean wasRecentlySurveyed() {
        return isRecentTime(KEY_LAST_SURVEYED);
    }

    private boolean isRecentTime(String key) {
        long eventTime = prefs().getLong(key, NOT_SET);

        if(eventTime == NOT_SET) {
            return false;
        }

        return new Date().getTime() - eventTime < Constants.DAY_IN_MILLIS * 90L;
    }

    public long getLastSeen() {
        return prefs().getLong(KEY_LAST_SEEN, NOT_SET);
    }

    public boolean isLastSeenSet() {
        return prefs().getLong(KEY_LAST_SEEN, NOT_SET) != NOT_SET;
    }

    public void setLastSeen(long lastSeen) {
        prefs().edit().putLong(KEY_LAST_SEEN, lastSeen).apply();
    }

    public void setLastSurveyed(long lastSurveyed) {
        prefs().edit().putLong(KEY_LAST_SURVEYED, lastSurveyed).apply();
    }

    public void setAccessToken(String accessToken) {
        prefs().edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        return prefs().getString(KEY_ACCESS_TOKEN, null);
    }

    public void clearAccessToken() {
        setAccessToken(null);
    }

    public void setEndUserId(long endUserId) {
        prefs().edit().putLong(KEY_END_USER_ID, endUserId).apply();
    }

    public long getEndUserId() {
        return prefs().getLong(KEY_END_USER_ID, INVALID_ID);
    }

    public void clearEndUserId() {
        setEndUserId(Constants.INVALID_ID);
    }

    public void setSelectedScore(int selectedScore) {
        prefs().edit().putInt(KEY_SELECTED_SCORE, selectedScore).apply();
    }

    public int getSelectedScore() {
        return prefs().getInt(KEY_SELECTED_SCORE, Constants.NOT_SET);
    }

    public void clearSelectedScore() {
        setSelectedScore(Constants.NOT_SET);
    }

    public void setFeedbackInputValue(String feedbackInputValue) {
        prefs().edit().putString(KEY_FEEDBACK_INPUT_VALUE, feedbackInputValue).apply();
    }

    public String getFeedbackInputValue() {
        return prefs().getString(KEY_FEEDBACK_INPUT_VALUE, null);
    }

    public void clearFeedbackInputValue() {
        setFeedbackInputValue(null);
    }

    public void setCurrentState(int currentState) {
        prefs().edit().putInt(KEY_CURRENT_STATE, currentState).apply();
    }

    public int getCurrentState() {
        return prefs().getInt(KEY_CURRENT_STATE, NOT_SET);
    }

    public void setResponseSent(boolean responseSent) {
        prefs().edit().putBoolean(KEY_RESPONSE_SENT, responseSent).apply();
    }

    public boolean getResponseSent() {
        return prefs().getBoolean(KEY_RESPONSE_SENT, false);
    }

    public void clear() {
        prefs().edit().clear().apply();
    }
}
