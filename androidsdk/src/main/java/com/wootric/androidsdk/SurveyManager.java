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

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyManager implements
        SurveyValidator.OnSurveyValidatedListener,
        GetAccessTokenTask.OnAccessTokenReceivedListener,
        GetEndUserTask.OnEndUserReceivedListener,
        CreateEndUserTask.OnEndUserCreatedListener {

    private static final String LOG_TAG = SurveyManager.class.getName();

    private final WeakReference<Context> mWeakContext;
    private final User mUser;
    private final EndUser mEndUser;
    private final String mOriginUrl;
    private final SurveyValidator mSurveyValidator;
    private final Settings mSettings;
    private final ConnectionUtils mConnectionUtils;
    private final PreferencesUtils mPreferencesUtils;

    SurveyManager(WeakReference<Context> weakContext, User user, EndUser endUser, String originUrl,
                  Settings settings, ConnectionUtils connectionUtils,
                  PreferencesUtils preferencesUtils, SurveyValidator surveyValidator) {

        mWeakContext = weakContext;
        mUser = user;
        mEndUser = endUser;
        mOriginUrl = originUrl;
        mSurveyValidator = surveyValidator;
        mSettings = settings;
        mConnectionUtils = connectionUtils;
        mPreferencesUtils = preferencesUtils;
    }

    void start() {
        showSurvey();
//        sendGetTrackingPixelRequest();
//        mPreferencesUtils.touchLastSeen();
//
//        validateSurvey();
    }

    void validateSurvey() {
        mSurveyValidator.setOnSurveyValidatedListener(this);
        mSurveyValidator.validate();
    }

    @Override
    public void onSurveyValidated(Settings surveyServerSettings) {
        mSettings.merge(surveyServerSettings);

        sendGetAccessTokenRequest();
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        if(accessToken == null) {
            Log.d(LOG_TAG, "NULL access token received");
            return;
        }

        mPreferencesUtils.setAccessToken(accessToken);
        sendGetEndUserRequest();
    }

    @Override
    public void onEndUserReceived(EndUser endUser) {
        if(endUser == null) {
            sendCreateEndUserRequest();
        } else {
            mEndUser.setId(endUser.getId());

            if(mEndUser.hasProperties()) {
                sendUpdateEndUserRequest();
            }

            showSurvey();
        }
    }

    @Override
    public void onEndUserCreated(EndUser endUser) {
        if(endUser == null) return;

        mEndUser.setId(endUser.getId());

        showSurvey();
    }

    private void showSurvey() {
        final Context context = mWeakContext.get();
        if(context == null) return;

        final String surveyDialogTag = "survey_dialog_tag";
        final FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        Fragment prev = fragmentManager.findFragmentByTag(surveyDialogTag);

        if(prev != null) {
            fragmentManager.beginTransaction().remove(prev).commit();
        }

        final SurveyFragment surveyFragment = SurveyFragment.newInstance(mUser, mEndUser,
                mOriginUrl, mSettings.getLocalizedTexts(), mSettings.getCustomMessage());

        int delayMillis = mSettings.getTimeDelay() * 1000;

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
        new GetTrackingPixelTask(mUser, mEndUser, mOriginUrl)
                .execute();
    }

    private void sendGetAccessTokenRequest() {
        new GetAccessTokenTask(mUser, this, mConnectionUtils)
                .execute();
    }

    private void sendGetEndUserRequest() {
        new GetEndUserTask(mEndUser, mPreferencesUtils.getAccessToken(), this, mConnectionUtils)
                .execute();
    }

    private void sendCreateEndUserRequest() {
        new CreateEndUserTask(mEndUser, mPreferencesUtils.getAccessToken(), this, mConnectionUtils)
                .execute();
    }

    private void sendUpdateEndUserRequest() {
        new UpdateEndUserTask(mEndUser, mPreferencesUtils.getAccessToken(), mConnectionUtils)
                .execute();
    }
}
