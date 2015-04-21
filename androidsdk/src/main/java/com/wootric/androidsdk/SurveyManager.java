package com.wootric.androidsdk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricCustomMessage;
import com.wootric.androidsdk.tasks.CreateEndUserTask;
import com.wootric.androidsdk.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.tasks.GetEndUserTask;
import com.wootric.androidsdk.utils.Blur;
import com.wootric.androidsdk.utils.ImageUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class SurveyManager implements
        SurveyValidator.OnSurveyValidatedListener,
        GetEndUserTask.OnEndUserReceivedListener,
        CreateEndUserTask.OnEndUserCreatedListener,
        GetAccessTokenTask.OnAccessTokenReceivedListener {

    private final WeakReference<Activity> weakActivity;
    private final SurveyValidator surveyValidator;
    private WootricCustomMessage mCustomMessage;

    private final String originUrl;
    private EndUser mEndUser;
    private User mUser;
    private String mAccessToken;

    SurveyManager(WeakReference<Activity> weakActivity, User user, EndUser endUser,
                  SurveyValidator surveyValidator, String originUrl) {

        if(weakActivity == null || surveyValidator == null || endUser == null || originUrl == null) {
            throw new IllegalArgumentException
                    ("Mandatory params cannot be null.");
        }

        this.weakActivity = weakActivity;
        this.surveyValidator = surveyValidator;
        this.originUrl = originUrl;

        mEndUser = endUser;
        mUser = user;
    }

    public SurveyManager surveyImmediately() {
        surveyValidator.setSurveyImmediately(true);
        return this;
    }

    public SurveyManager createdAt(long createdAt) {
        mEndUser.setCreatedAt(createdAt);
        return this;
    }

    public SurveyManager dailyResponseCap(int dailyResponseCap) {
        surveyValidator.setDailyResponseCap(dailyResponseCap);
        return this;
    }

    public SurveyManager registeredPercent(int registeredPercent) {
        surveyValidator.setRegisteredPercent(registeredPercent);
        return this;
    }

    public SurveyManager visitorPercent(int visitorPercent) {
        surveyValidator.setVisitorPercent(visitorPercent);
        return this;
    }

    public SurveyManager resurveyThrottle(int resurveyThrottle) {
        surveyValidator.setResurveyThrottle(resurveyThrottle);
        return this;
    }

    public SurveyManager customMessage(WootricCustomMessage customMessage) {
        this.mCustomMessage = customMessage;
        return this;
    }

    public void survey() {
        updateLastSeen();
        setupSurveyValidator();
    }

    void setupSurveyValidator() {
        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    void updateLastSeen() {
        final Activity activity = weakActivity.get();

        if(activity != null) {
            PreferencesUtils prefs = PreferencesUtils.getInstance(activity);

            if(!prefs.wasRecentlyLastSeen()) {
                prefs.setLastSeen(new Date().getTime());
            }
        }
    }

    @Override
    public void onSurveyValidated() {
        new GetAccessTokenTask(
                mUser.getClientId(),
                mUser.getClientSecret(),
                this
        ).execute();
    }

    @Override
    public void onAccessTokenReceived(String accessToken) {
        mAccessToken = accessToken;

        new GetEndUserTask(
                mEndUser.getEmail(),
                accessToken,
                this
        ).execute();
    }

    @Override
    public void onEndUserReceived(EndUser endUser) {
        if(endUser == null) {
            new CreateEndUserTask(mEndUser, mAccessToken, this).execute();
        } else {
            setupSurveyForCurrentView(endUser);
        }
    }

    @Override
    public void onEndUserCreated(EndUser endUser) {
        if(endUser != null) {
            setupSurveyForCurrentView(endUser);
        }
    }

    private void setupSurveyForCurrentView(EndUser endUser) {
        mEndUser = endUser;

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

        if(activity != null) {
            Bitmap screenshot = ImageUtils.takeActivityScreenshot(activity, 4);
            Bitmap blurredScreenshot = Blur.blur(activity, screenshot, 8);
            SurveyActivity.start(activity, blurredScreenshot, mAccessToken, mUser, mEndUser, originUrl, mCustomMessage);
        }
    }
}
