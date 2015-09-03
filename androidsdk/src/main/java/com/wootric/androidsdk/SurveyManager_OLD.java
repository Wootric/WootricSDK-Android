package com.wootric.androidsdk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;

import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.tasks.BlurBackgroundTask;
import com.wootric.androidsdk.tasks.CreateEndUserTask;
import com.wootric.androidsdk.tasks.CreateResponseTask;
import com.wootric.androidsdk.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.tasks.GetEndUserTask;
import com.wootric.androidsdk.tasks.GetTrackingPixelTask;
import com.wootric.androidsdk.tasks.UpdateEndUserTask;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class SurveyManager_OLD implements
        SurveyValidator_OLD.OnSurveyValidatedListener,
        GetEndUserTask.OnEndUserReceivedListener,
        CreateEndUserTask.OnEndUserCreatedListener,
        GetAccessTokenTask.OnAccessTokenReceivedListener {

    private final WeakReference<Activity> weakActivity;
    final SurveyValidator_OLD surveyValidatorOLD;

    private final String mOriginUrl;
    private User mUser;

    private final Settings mSettings = new Settings();

    private EndUser mEndUser;

    private boolean mActivityValid = true;

    private final PreferencesUtils prefs;
    private final ConnectionUtils connectionUtils;

    SurveyManager_OLD(WeakReference<Activity> weakActivity, User user, EndUser endUser,
                      SurveyValidator_OLD surveyValidatorOLD, String mOriginUrl,
                      PreferencesUtils prefs, ConnectionUtils connectionUtils) {

        if(weakActivity == null || surveyValidatorOLD == null || endUser == null || mOriginUrl == null) {
            throw new IllegalArgumentException
                    ("Mandatory params cannot be null.");
        }

        this.weakActivity = weakActivity;
        this.surveyValidatorOLD = surveyValidatorOLD;
        this.mOriginUrl = mOriginUrl;
        this.prefs = prefs;
        this.connectionUtils = connectionUtils;

        mEndUser = endUser;
        mUser = user;
    }

    public SurveyManager_OLD surveyImmediately() {
        surveyValidatorOLD.setSurveyImmediately(true);
        return this;
    }

    public SurveyManager_OLD createdAt(long createdAt) {
        mEndUser.setCreatedAt(createdAt);
        return this;
    }

    public SurveyManager_OLD dailyResponseCap(int dailyResponseCap) {
        surveyValidatorOLD.setDailyResponseCap(dailyResponseCap);
        return this;
    }

    public SurveyManager_OLD registeredPercent(int registeredPercent) {
        surveyValidatorOLD.setRegisteredPercent(registeredPercent);
        return this;
    }

    public SurveyManager_OLD visitorPercent(int visitorPercent) {
        surveyValidatorOLD.setVisitorPercent(visitorPercent);
        return this;
    }

    public SurveyManager_OLD resurveyThrottle(int resurveyThrottle) {
        surveyValidatorOLD.setResurveyThrottle(resurveyThrottle);
        return this;
    }

    public SurveyManager_OLD customMessage(CustomMessage customMessage) {
        mSettings.setCustomMessage(customMessage);
        return this;
    }

    public SurveyManager_OLD productName(String productName) {
        mSettings.setProductName(productName);
        return this;
    }

    public SurveyManager_OLD forceSurvey() {
        surveyValidatorOLD.forceSurvey();
        return this;
    }

    EndUser getEndUser() {
        return mEndUser;
    }

    public void survey() {
        sendGetTrackingPixelRequest();
        prefs.touchLastSeen();

        checkUnsentResponse();
        setupSurveyValidator();
    }

    private void checkUnsentResponse() {
        if(prefs.getUnsentResponse() == null) {
            return;
        }

        if(prefs.getAccessToken() == null) {
            sendGetAccessTokenRequest();
        } else if(prefs.getEndUserId() == Constants.INVALID_ID) {
            sendGetEndUserRequest();
        } else {
            sendUnsentResponse();
        }
    }

    private void sendUnsentResponse() {
        String unsentResponse = prefs.getUnsentResponse();

        if(unsentResponse == null){
            return;
        }

        try {
            JSONObject obj = new JSONObject(unsentResponse);

            new CreateResponseTask(
                    prefs.getAccessToken(),
                    EndUser.fromJson(obj.getJSONObject("endUser")),
                    obj.getString("originUrl"),
                    obj.getInt("score"),
                    obj.getString("text"),
                    connectionUtils,
                    prefs
            ).execute();

            prefs.clearUnsentResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isAlreadyAuthorized() {
        return prefs.getAccessToken() != null && prefs.getEndUserId() != Constants.INVALID_ID;
    }

    void setupSurveyValidator() {
        surveyValidatorOLD.setOnSurveyValidatedListener(this);
        surveyValidatorOLD.validate();
    }

    @Override
    public void onSurveyValidated(Settings settings) {
        mSettings.merge(settings);

        if(!mSettings.firstSurveyDelayPassed(mEndUser.getCreatedAt())) {
            return;
        }

        if(isAlreadyAuthorized()) {
            mEndUser.setId(prefs.getEndUserId());

            setupSurveyForCurrentView();
        } else {
            sendGetAccessTokenRequest();
        }
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        prefs.setAccessToken(accessToken);
        checkUnsentResponse();

        sendGetEndUserRequest();
    }

    private void sendGetEndUserRequest() {
        new GetEndUserTask(
                mEndUser.getEmail(),
                prefs.getAccessToken(),
                this,
                connectionUtils
        ).execute();
    }

    private void sendGetAccessTokenRequest() {
        new GetAccessTokenTask(
                mUser,
                this,
                connectionUtils
        ).execute();
    }

    private void sendGetTrackingPixelRequest() {
        new GetTrackingPixelTask(
                mUser,
                mEndUser,
                mOriginUrl
        ).execute();
    }

    private void sendCreateEndUserRequest() {
        new CreateEndUserTask(
                mEndUser,
                prefs.getAccessToken(),
                this,
                connectionUtils
        ).execute();
    }

    private void sendUpdateEndUserRequest() {
        new UpdateEndUserTask(
                mEndUser,
                prefs.getAccessToken(),
                connectionUtils
        ).execute();
    }

    @Override
    public void onEndUserReceived(EndUser endUser) {
        if(endUser == null) {
            sendCreateEndUserRequest();
        } else {
            mEndUser.setId(endUser.getId());

            prefs.setEndUserId(mEndUser.getId());

            if(mEndUser.hasProperties()) {
                sendUpdateEndUserRequest();
            }

            setupSurveyForCurrentView();
        }
    }

    void invalidateActivity() {
        mActivityValid = false;
    }

    boolean isActivityValid() {
        return mActivityValid;
    }

    @Override
    public void onEndUserCreated(EndUser endUser) {
        if(endUser == null) {
            return;
        }

        mEndUser = endUser;
        prefs.setEndUserId(mEndUser.getId());
        setupSurveyForCurrentView();
    }

    private void setupSurveyForCurrentView() {
        final Activity activity = weakActivity.get();

        if(activity != null) {
            final View view = activity.findViewById(android.R.id.content);

            if(view.getHeight() > 0) {
                startSurveyActivity();
            } else {
                view.getViewTreeObserver().addOnPreDrawListener(startSurveyBeforeDrawListener(view));
            }
        }
    }

    private ViewTreeObserver.OnPreDrawListener startSurveyBeforeDrawListener(final View view) {
        return new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewTreeObserver observer = view.getViewTreeObserver();
                if(observer != null) {
                    observer.removeOnPreDrawListener(this);
                }

                startSurveyActivity();

                return true;
            }
        };
    }

    private void startSurveyActivity() {
        final Activity activity = weakActivity.get();

        if(activity != null && isActivityValid()) {
            if(mSettings.getTimeDelay() > 0) {
                startActivityWithDelay(activity, mSettings.getTimeDelay());
            } else {
                prepareSurveyActivity();
            }
        }
    }

    private void prepareSurveyActivity() {
        new BlurBackgroundTask(weakActivity) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                startSurveyActivity(bitmap);
            }
        }.execute();
    }

    private void startSurveyActivity(Bitmap background) {
        Activity activity = weakActivity.get();
        if(activity != null && !activity.isFinishing() && mActivityValid) {
            SurveyActivity.start(activity, background, mUser, mEndUser, mOriginUrl, mSettings);
        }
    }

    private void startActivityWithDelay(final Activity activity, int delayInSeconds) {
        int delayMillis = delayInSeconds * 1000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareSurveyActivity();
            }
        }, delayMillis);
    }
}
