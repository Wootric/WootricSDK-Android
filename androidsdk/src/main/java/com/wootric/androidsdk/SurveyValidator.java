package com.wootric.androidsdk;

import android.content.Context;

import java.util.Date;

import static com.wootric.androidsdk.Constants.DEFAULT;

/**
 * Validates if a survey should be shown to end user
 * Created by maciejwitowski on 4/12/15.
 */
public class SurveyValidator {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    private final String accountToken;
    private final String endUserEmail;
    private final Context context;

    // Optional
    private boolean surveyImmediately   = false;
    private long createdAt              = DEFAULT;
    private int dailyResponseCap        = DEFAULT;
    private int registeredPercent       = DEFAULT;
    private int visitorPercent          = DEFAULT;
    private int resurveyThrottle        = DEFAULT;

    private static final int FIRST_SURVEY = 31*60*60*24*1000; // 31 days

    SurveyValidator(Context context, String accountToken, String endUserEmail) {
        this.context = context;
        this.accountToken = accountToken;
        this.endUserEmail = endUserEmail;
    }

    void setSurveyImmediately(boolean surveyImmediately) {
        this.surveyImmediately = surveyImmediately;
    }

    void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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
        if(needsSurvey()) {
            if(surveyImmediately) {
                notifySurveyValidated(true);
            } else {
                checkEligibility();
            }
        }
    }

    private boolean needsSurvey() {
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        return !prefs.wasRecentlySurveyed() ||
                surveyImmediately ||
                createdAt == DEFAULT ||
                firstSurveyDelayPassed() ||
                dayDelayPassed();
    }

    private boolean firstSurveyDelayPassed() {
        long userAge = new Date().getTime() - createdAt*1000;
        return userAge >= FIRST_SURVEY;
    }

    private boolean dayDelayPassed(){
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        long timeSinceLastSeen = new Date().getTime() - prefs.getLastSeen();
        return timeSinceLastSeen >= FIRST_SURVEY;
    }

    private void checkEligibility() {
        CheckEligibilityTask checkEligibilityTask = new CheckEligibilityTask(
                accountToken, endUserEmail, dailyResponseCap,
                registeredPercent, visitorPercent, resurveyThrottle) {
            @Override
            protected void onPostExecute(Boolean eligible) {
                notifySurveyValidated(eligible);
            }
        };

        checkEligibilityTask.execute();
    }

    private void notifySurveyValidated(boolean shouldShowSurvey) {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated(shouldShowSurvey);
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated(boolean shouldShowSurvey);
    }
}
