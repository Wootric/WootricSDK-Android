package com.wootric.androidsdk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
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

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements
        SurveyValidator.OnSurveyValidatedListener,
        WootricApiCallback {

    private static final String LOG_TAG = SurveyManager.class.getName();

    private final WeakReference<Context> weakContext;
    private final WootricRemoteClient wootricApiClient;
    private final User user;
    private final EndUser endUser;
    private final SurveyValidator surveyValidator;
    private final Settings settings;
    private final PreferencesUtils preferencesUtils;

    private String accessToken;
    private String originUrl;

    private static final String SURVEY_DIALOG_TAG = "survey_dialog_tag";


    SurveyManager(WeakReference<Context> weakContext, WootricRemoteClient wootricApiClient, User user, EndUser endUser,
                  Settings settings, PreferencesUtils preferencesUtils,
                  SurveyValidator surveyValidator) {

        this.weakContext = weakContext;
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
        Wootric.notifySurveyFinished();
    }

    @Override
    public void onAuthenticateSuccess(String accessToken) {
        if(accessToken == null) return;

        setAccessToken(accessToken);
        sendGetEndUserRequest();
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
        Wootric.notifySurveyFinished();
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
        wootricApiClient.getEndUserByEmail(endUser.getEmail(), accessToken, this);
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
                    Wootric.notifySurveyFinished();
                }
            }
        }, settings.getTimeDelayInMillis());
    }

    private void showSurveyFragment() {
        final Context context = weakContext.get();

        if(context == null) return;

        final FragmentManager fragmentManager = ((Activity) context).getFragmentManager();

        SurveyFragment surveyFragment = SurveyFragment.newInstance(user, endUser, getOriginUrl(),
                accessToken, settings);

        final boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if(isTablet) {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, surveyFragment)
                    .setCustomAnimations(R.anim.slide_up_dialog, R.anim.slide_down_dialog)
                    .addToBackStack(null).commit();
        } else {
            surveyFragment.show(fragmentManager, SURVEY_DIALOG_TAG);
        }
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String getOriginUrl() {
        if(originUrl == null) {
            final Context context = weakContext.get();

            if(context != null) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo;
                try {
                    appInfo = pm.getApplicationInfo(context.getApplicationInfo().packageName, 0);
                    originUrl = pm.getApplicationLabel(appInfo).toString();
                } catch (Exception e) {
                }
            }
        }

        if(originUrl == null) originUrl = "";

        return originUrl;
    }
}
