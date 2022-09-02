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
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_SCORE = "score";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_TEXT = "text";
    private static final String KEY_UNIQUE_LINK = "survey[unique_link]";
    private static final String KEY_LANGUAGE = "survey[language]";

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
                    jsonResponse.getLong(KEY_USER_ID),
                    jsonResponse.getLong(KEY_ACCOUNT_ID),
                    accessToken,
                    jsonResponse.getString(KEY_ORIGIN_URL),
                    jsonResponse.getInt(KEY_SCORE),
                    jsonResponse.getInt(KEY_PRIORITY),
                    jsonResponse.getString(KEY_TEXT),
                    jsonResponse.getString(KEY_UNIQUE_LINK),
                    jsonResponse.getString(KEY_LANGUAGE)
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
                    jsonDecline.getLong(KEY_USER_ID),
                    jsonDecline.getLong(KEY_ACCOUNT_ID),
                    jsonDecline.getInt(KEY_PRIORITY),
                    accessToken,
                    jsonDecline.getString(KEY_ORIGIN_URL),
                    jsonDecline.getString(KEY_UNIQUE_LINK)
            );

            Log.d(LOG_TAG, "Processed offline Decline with data: " + offlineDecline);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        preferencesUtils.putDecline(null);
    }

    public void saveOfflineResponse(long endUserId, long userId, long accountId, String originUrl, int score, int priority, String text, String uniqueLink, String language) {
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put(KEY_END_USER_ID, endUserId);
            jsonResponse.put(KEY_USER_ID, userId);
            jsonResponse.put(KEY_ACCOUNT_ID, accountId);
            jsonResponse.put(KEY_ORIGIN_URL, originUrl);
            jsonResponse.put(KEY_SCORE, score);
            jsonResponse.put(KEY_PRIORITY, priority);
            jsonResponse.put(KEY_TEXT, text);
            jsonResponse.put(KEY_UNIQUE_LINK, uniqueLink);
            jsonResponse.put(KEY_LANGUAGE, language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String stringResponse = jsonResponse.toString();
        preferencesUtils.putResponse(jsonResponse.toString());
        Log.d(LOG_TAG, "Saved offline Response with data: " + stringResponse);

    }

    public void saveOfflineDecline(long endUserId, long userId, long accountId, int priority, String originUrl, String uniqueLink) {
        JSONObject jsonDecline = new JSONObject();
        try {
            jsonDecline.put(KEY_END_USER_ID, endUserId);
            jsonDecline.put(KEY_USER_ID, userId);
            jsonDecline.put(KEY_ACCOUNT_ID, accountId);
            jsonDecline.put(KEY_PRIORITY, priority);
            jsonDecline.put(KEY_ORIGIN_URL, originUrl);
            jsonDecline.put(KEY_UNIQUE_LINK, uniqueLink);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String stringDecline = jsonDecline.toString();
        preferencesUtils.putDecline(stringDecline);
        Log.d(LOG_TAG, "Saved offline Decline with data: " + stringDecline);
    }
}
