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

import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.wootric.androidsdk.network.WootricApiCallback;
import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.views.SurveyFragment;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements SurveyValidator.OnSurveyValidatedListener, WootricApiCallback {

    private static final String LOG_TAG = "WOOTRIC_SDK";

    private final Activity activity;
    private final WootricRemoteClient wootricApiClient;
    private final User user;
    private final EndUser endUser;
    private final SurveyValidator surveyValidator;
    private final Settings settings;
    private final PreferencesUtils preferencesUtils;

    private String accessToken;
    private String originUrl;

    private static final String SURVEY_DIALOG_TAG = "survey_dialog_tag";

    SurveyManager(Activity activity, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
                  Settings settings, PreferencesUtils preferencesUtils,
                  SurveyValidator surveyValidator) {

        this.activity = activity;
        this.wootricApiClient = wootricApiClient;
        this.user = user;
        this.endUser = endUser;
        this.surveyValidator = surveyValidator;
        this.settings = settings;
        this.preferencesUtils = preferencesUtils;
    }

    void start() {
        sendGetTrackingPixelRequest();
        preferencesUtils.touchLastSeen();

        validateSurvey();
    }

    @Override
    public void onSurveyValidated(Settings surveyServerSettings) {
        settings.mergeWithSurveyServerSettings(surveyServerSettings);

        sendGetAccessTokenRequest();
    }

    @Override
    public void onSurveyNotNeeded() {
        Wootric.notifySurveyFinished(false);
    }

    @Override
    public void onAuthenticateSuccess(String accessToken) {
        if(accessToken == null) {
            Wootric.notifySurveyFinished(false);
            return;
        }

        setAccessToken(accessToken);
        sendOfflineData();
        sendGetEndUserRequest();
    }

    private void sendOfflineData() {
        wootricApiClient.processOfflineData(accessToken);
    }

    @Override
    public void onGetEndUserIdSuccess(long endUserId) {
        endUser.setId(endUserId);

        if(this.endUser.hasProperties()) {
            sendUpdateEndUserRequest();
        }

        showSurvey();
    }

    @Override
    public void onEndUserNotFound() {
        sendCreateEndUserRequest();
    }

    @Override
    public void onCreateEndUserSuccess(long endUserId) {
        this.endUser.setId(endUserId);

        showSurvey();
    }

    @Override
    public void onApiError(Exception error) {
        Log.d(LOG_TAG, error.getLocalizedMessage());
        Wootric.notifySurveyFinished(false);
    }

    private void validateSurvey() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    private void sendGetTrackingPixelRequest() {
        wootricApiClient.getTrackingPixel(user, endUser, getOriginUrl());
    }

    private void sendGetAccessTokenRequest() {
        wootricApiClient.authenticate(user, this);
    }

    private void sendGetEndUserRequest() {
        wootricApiClient.getEndUserByEmail(endUser.getEmailOrUnknown(), accessToken, this);
    }

    private void sendCreateEndUserRequest() {
        wootricApiClient.createEndUser(endUser, accessToken, this);
    }

    private void sendUpdateEndUserRequest() {
        wootricApiClient.updateEndUser(endUser, accessToken, this);
    }

    void showSurvey() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    showSurveyFragment();
                } catch (IllegalStateException e) {
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                    Wootric.notifySurveyFinished(false);
                }
            }
        }, settings.getTimeDelayInMillis());
    }

    private void showSurveyFragment() {
        try {
            final FragmentManager fragmentManager = activity.getFragmentManager();

            SurveyFragment surveyFragment = SurveyFragment.newInstance(endUser, getOriginUrl(),
                    accessToken, settings, user);

            final boolean isTablet = activity.getResources().getBoolean(R.bool.isTablet);

            if(isTablet) {
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, surveyFragment)
                        .setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_down_dialog)
                        .addToBackStack(null).commit();
            } else {
                surveyFragment.show(fragmentManager, SURVEY_DIALOG_TAG);
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "showSurveyFragment: " + e.toString());
            e.printStackTrace();
        }
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getOriginUrl() {
        if(originUrl == null) {
            PackageManager pm;
            ApplicationInfo appInfo;

            try {
                pm = activity.getPackageManager();
                appInfo = pm.getApplicationInfo(activity.getApplicationInfo().packageName, 0);
                originUrl = pm.getApplicationLabel(appInfo).toString();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
                e.printStackTrace();
            }
        }

        if(originUrl == null) {
            originUrl = "";
        }

        return originUrl;
    }
}
