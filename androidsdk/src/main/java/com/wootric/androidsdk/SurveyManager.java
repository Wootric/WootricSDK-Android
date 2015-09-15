package com.wootric.androidsdk;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.wootric.androidsdk.network.TrackingPixelClient;
import com.wootric.androidsdk.network.WootricApiClient;
import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;
import com.wootric.androidsdk.views.SurveyFragment;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements
        SurveyValidator.OnSurveyValidatedListener,
        WootricApiClient.WootricApiCallback {

    private static final String LOG_TAG = SurveyManager.class.getName();

    private final Context context;
    private final WootricApiClient wootricApiClient;
    private final TrackingPixelClient trackingPixelClient;
    private final User user;
    private final EndUser endUser;
    private final SurveyValidator surveyValidator;
    private final Settings settings;
    private final PreferencesUtils preferencesUtils;

    private String accessToken;
    private final String originUrl;

    private static final String SURVEY_DIALOG_TAG = "survey_dialog_tag";


    SurveyManager(Context context, WootricApiClient wootricApiClient, TrackingPixelClient trackingPixelClient, User user, EndUser endUser,
                  Settings settings, String originUrl, PreferencesUtils preferencesUtils,
                  SurveyValidator surveyValidator) {

        this.context = context;
        this.wootricApiClient = wootricApiClient;
        this.trackingPixelClient = trackingPixelClient;
        this.user = user;
        this.endUser = endUser;
        this.surveyValidator = surveyValidator;
        this.settings = settings;
        this.originUrl = originUrl;
        this.preferencesUtils = preferencesUtils;
    }

    boolean start() {
        sendGetTrackingPixelRequest();
        preferencesUtils.touchLastSeen();

        validateSurvey();

        // TODO Don't return true and test it in some other way
        return true;
    }

    @Override
    public void onSurveyValidated(Settings surveyServerSettings) {
        settings.mergeWithSurveyServerSettings(surveyServerSettings);

        sendGetAccessTokenRequest();
    }

    @Override
    public void onAuthenticateSuccess(AuthenticationResponse authenticationResponse) {
        setAccessToken(authenticationResponse.accessToken);
        sendGetEndUserRequest();
    }

    @Override
    public void onGetEndUserSuccess(List<EndUser> endUsers) {
        if(endUsers.size() > 0) {
            long endUserId = endUsers.get(0).getId();
            endUser.setId(endUserId);

            if(this.endUser.hasProperties()) {
                sendUpdateEndUserRequest();
            }

            showSurvey();
        } else {
            sendCreateEndUserRequest();
        }
    }

    @Override
    public void onCreateEndUserSuccess(EndUser endUser) {
        this.endUser.setId(endUser.getId());

        showSurvey();
    }

    @Override
    public void onApiError(RetrofitError error) {
        // TODO Handle API Request Error
    }


    private void validateSurvey() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    private void sendGetTrackingPixelRequest() {
        trackingPixelClient.getTrackingPixel(user, endUser, originUrl);
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
        wootricApiClient.updateEndUser(endUser, accessToken);
    }

    void showSurvey() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    showSurveyFragment();
                } catch (IllegalStateException e) {
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                }
            }
        }, settings.getTimeDelayInMillis());
    }

    private void showSurveyFragment() {
        final FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        Fragment prev = fragmentManager.findFragmentByTag(SURVEY_DIALOG_TAG);

        if(prev != null) {
            fragmentManager.beginTransaction().remove(prev).commit();
        }

        SurveyFragment surveyFragment = SurveyFragment.newInstance(user, endUser,
                originUrl, settings.getLocalizedTexts(), settings.getCustomMessage());

        surveyFragment.show(fragmentManager, SURVEY_DIALOG_TAG);
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
