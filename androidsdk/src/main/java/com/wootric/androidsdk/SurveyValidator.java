package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.tasks.CheckEligibilityTask;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.Date;

import static com.wootric.androidsdk.utils.Constants.NOT_SET;

/**
 * Validates if a survey should be shown to end user
 * Created by maciejwitowski on 4/12/15.
 */
public class SurveyValidator {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    private final User user;
    private final EndUser endUser;
    private final WeakReference<Activity> weakActivity;

    // Optional
    private boolean surveyImmediately   = false;
    private int dailyResponseCap        = NOT_SET;
    private int registeredPercent       = NOT_SET;
    private int visitorPercent          = NOT_SET;
    private int resurveyThrottle        = NOT_SET;

    private static final int FIRST_SURVEY = 31*60*60*24*1000; // 31 days

    SurveyValidator(WeakReference<Activity> context, User user, EndUser endUser) {
        this.weakActivity = context;
        this.user = user;
        this.endUser = endUser;
    }

    void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    void setDailyResponseCap(int dailyResponseCap) {
        this.dailyResponseCap = dailyResponseCap;
    }

    void setRegisteredPercent(int registeredPercent) {
        this.registeredPercent = registeredPercent;
    }

    void setVisitorPercent(int visitorPercent) {
        this.visitorPercent = visitorPercent;
    }

    void setResurveyThrottle(int resurveyThrottle) {
        this.resurveyThrottle = resurveyThrottle;
    }

    public void setOnSurveyValidatedListener(OnSurveyValidatedListener onSurveyValidatedListener) {
        this.onSurveyValidatedListener = onSurveyValidatedListener;
    }

    void validate() {
        final Context context = getContext();

        if(context == null) {
            return;
        }

        if(needsSurvey(context)) {
            if(surveyImmediately) {
                notifyShouldShowSurvey();
            } else {
                checkEligibility();
            }
        }
    }

    private Context getContext() {
        return (weakActivity != null ? weakActivity.get() : null);
    }

    boolean needsSurvey(Context context) {
        boolean wasRecentlySurveyed = PreferencesUtils.getInstance(context).wasRecentlySurveyed();

        return !wasRecentlySurveyed ||
                surveyImmediately ||
                endUser.getCreatedAt() == NOT_SET ||
                firstSurveyDelayPassed() ||
                dayDelayPassed(context);
    }

    private boolean firstSurveyDelayPassed() {
        long userAge = new Date().getTime() - endUser.getCreatedAt()*1000;
        return userAge >= FIRST_SURVEY;
    }

    private boolean dayDelayPassed(Context context){
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        if(!prefs.isLastSeenSet()) {
            return false;
        }

        long timeSinceLastSeen = new Date().getTime() - prefs.getLastSeen();

        return timeSinceLastSeen >= FIRST_SURVEY;
    }

    void checkEligibility() {
        CheckEligibilityTask checkEligibilityTask = new CheckEligibilityTask(
                user.getAccountToken(), endUser, dailyResponseCap,
                registeredPercent, visitorPercent, resurveyThrottle) {
            @Override
            protected void onPostExecute(Boolean eligible) {
                if(eligible) {
                    notifyShouldShowSurvey();
                }
            }
        };

        checkEligibilityTask.execute();
    }

    void notifyShouldShowSurvey() {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated();
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated();
    }
}
