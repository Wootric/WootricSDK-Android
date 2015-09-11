package com.wootric.androidsdk;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.tasks.CreateEndUserTask;
import com.wootric.androidsdk.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.tasks.GetEndUserTask;
import com.wootric.androidsdk.tasks.GetTrackingPixelTask;
import com.wootric.androidsdk.tasks.UpdateEndUserTask;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements
        SurveyValidator.OnSurveyValidatedListener,
        GetAccessTokenTask.OnAccessTokenReceivedListener,
        GetEndUserTask.OnEndUserReceivedListener,
        CreateEndUserTask.OnEndUserCreatedListener {

    private static final String LOG_TAG = SurveyManager.class.getName();

    final Context context;
    final User user;
    final EndUser endUser;
    final SurveyValidator surveyValidator;
    final Settings settings;
    final ConnectionUtils connectionUtils;
    final PreferencesUtils preferencesUtils;

    private String accessToken;
    String originUrl;

    SurveyManager(Context context, User user, EndUser endUser,
                  Settings settings, String originUrl, ConnectionUtils connectionUtils,
                  PreferencesUtils preferencesUtils, SurveyValidator surveyValidator) {

        this.context = context;
        this.user = user;
        this.endUser = endUser;
        this.surveyValidator = surveyValidator;
        this.settings = settings;
        this.originUrl = originUrl;
        this.connectionUtils = connectionUtils;
        this.preferencesUtils = preferencesUtils;
    }

    boolean start() {
        sendGetTrackingPixelRequest();
        preferencesUtils.touchLastSeen();

        validateSurvey();

        return true;
    }

    private void validateSurvey() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    @Override
    public void onSurveyValidated(Settings surveyServerSettings) {
        settings.merge(surveyServerSettings);

        sendGetAccessTokenRequest();
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        if(accessToken == null) {
            Log.d(LOG_TAG, "NULL access token received");
            return;
        }

        this.accessToken = accessToken;
        sendGetEndUserRequest();
    }

    @Override
    public void onEndUserReceived(EndUser endUser) {
        if(endUser == null) {
            sendCreateEndUserRequest();
        } else {
            this.endUser.setId(endUser.getId());

            if(this.endUser.hasProperties()) {
                sendUpdateEndUserRequest();
            }

            showSurvey();
        }
    }

    @Override
    public void onEndUserCreated(EndUser endUser) {
        if(endUser == null) return;

        this.endUser.setId(endUser.getId());

        showSurvey();
    }

    private void showSurvey() {
        final String surveyDialogTag = "survey_dialog_tag";
        final FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        Fragment prev = fragmentManager.findFragmentByTag(surveyDialogTag);

        if(prev != null) {
            fragmentManager.beginTransaction().remove(prev).commit();
        }

        final SurveyFragment surveyFragment = SurveyFragment.newInstance(user, endUser,
                originUrl, settings.getLocalizedTexts(), settings.getCustomMessage());

        int delayMillis = settings.getTimeDelay() * 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    surveyFragment.show(fragmentManager, surveyDialogTag);
                } catch (IllegalStateException e) {
                    Log.d(LOG_TAG, e.getLocalizedMessage());
                }
            }
        }, delayMillis);
    }

    private void sendGetTrackingPixelRequest() {
        new GetTrackingPixelTask(user, endUser, originUrl)
                .execute();
    }

    private void sendGetAccessTokenRequest() {
        new GetAccessTokenTask(user, this, connectionUtils)
                .execute();
    }

    private void sendGetEndUserRequest() {
        new GetEndUserTask(endUser, accessToken, this, connectionUtils)
                .execute();
    }

    private void sendCreateEndUserRequest() {
        new CreateEndUserTask(endUser, accessToken, this, connectionUtils)
                .execute();
    }

    private void sendUpdateEndUserRequest() {
        new UpdateEndUserTask(endUser, accessToken, connectionUtils)
                .execute();
    }
}
