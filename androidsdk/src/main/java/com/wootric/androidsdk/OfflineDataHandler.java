package com.wootric.androidsdk;

import android.util.Log;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/30/15.
 */
public class OfflineDataHandler {

    private static final String LOG_TAG = OfflineDataHandler.class.getName();

    private static final String KEY_ORIGIN_URL = "origin_url";
    private static final String KEY_END_USER_ID = "end_user_id";
    private static final String KEY_SCORE = "score";
    private static final String KEY_TEXT = "text";

    private final PreferencesUtils preferencesUtils;

    public OfflineDataHandler(PreferencesUtils preferencesUtils) {
        this.preferencesUtils = preferencesUtils;
    }

    public void processOfflineData(WootricRemoteClient wootricRemoteClient, String accessToken) {
        processResponse(wootricRemoteClient, accessToken);
        processDecline(wootricRemoteClient, accessToken);
    }

    private void processResponse(WootricRemoteClient wootricRemoteClient, String accessToken) {
        String offlineResponse = preferencesUtils.getResponse();
        if(offlineResponse == null) return;

        try {
            JSONObject jsonResponse = new JSONObject(preferencesUtils.getResponse());
            wootricRemoteClient.createResponse(
                    jsonResponse.getLong(KEY_END_USER_ID),
                    accessToken,
                    jsonResponse.getString(KEY_ORIGIN_URL),
                    jsonResponse.getInt(KEY_SCORE),
                    jsonResponse.getString(KEY_TEXT)
            );

            Log.d(LOG_TAG, "Processed offline Response with data: " + offlineResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        preferencesUtils.putResponse(null);
    }

    private void processDecline(WootricRemoteClient wootricRemoteClient, String accessToken) {
        String offlineDecline = preferencesUtils.getDecline();
        if(offlineDecline == null) return;

        try {
            JSONObject jsonDecline = new JSONObject(preferencesUtils.getDecline());
            wootricRemoteClient.createDecline(
                    jsonDecline.getLong(KEY_END_USER_ID),
                    accessToken,
                    jsonDecline.getString(KEY_ORIGIN_URL)
            );

            Log.d(LOG_TAG, "Processed offline Decline with data: " + offlineDecline);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        preferencesUtils.putDecline(null);
    }

    public void saveOfflineResponse(long endUserId, String originUrl, int score, String text) {
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put(KEY_END_USER_ID, endUserId);
            jsonResponse.put(KEY_ORIGIN_URL, originUrl);
            jsonResponse.put(KEY_SCORE, score);
            jsonResponse.put(KEY_TEXT, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String stringResponse = jsonResponse.toString();
        preferencesUtils.putResponse(jsonResponse.toString());
        Log.d(LOG_TAG, "Saved offline Response with data: " + stringResponse);

    }

    public void saveOfflineDecline(long endUserId, String originUrl) {
        JSONObject jsonDecline = new JSONObject();
        try {
            jsonDecline.put(KEY_END_USER_ID, endUserId);
            jsonDecline.put(KEY_ORIGIN_URL, originUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String stringDecline = jsonDecline.toString();
        preferencesUtils.putDecline(stringDecline);
        Log.d(LOG_TAG, "Saved offline Decline with data: " + stringDecline);
    }
}
