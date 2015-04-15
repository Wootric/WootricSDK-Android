package com.wootric.androidsdk;

import android.content.Context;

import java.util.Date;

import static com.wootric.androidsdk.Constants.NOT_SET;

/**
 * Validates if a survey should be shown to end user
 * Created by maciejwitowski on 4/12/15.
 */
public class SurveyValidator {

    OnSurveyValidatedListener onSurveyValidatedListener;
    private final String accountToken;
    private final String endUserEmail;
    private final Context context;

    // Optional
    private boolean surveyImmediately   = false;
    private long createdAt              = NOT_SET;
    private int dailyResponseCap        = NOT_SET;
    private int registeredPercent       = NOT_SET;
    private int visitorPercent          = NOT_SET;
    private int resurveyThrottle        = NOT_SET;

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
                notifyShouldShowSurvey();
            } else {
                checkEligibility();
            }
        }
    }

    boolean needsSurvey() {
        boolean wasRecentlySurveyed = PreferencesUtils.getInstance(context).wasRecentlySurveyed();

        return !wasRecentlySurveyed ||
                surveyImmediately ||
                createdAt == NOT_SET ||
                firstSurveyDelayPassed() ||
                dayDelayPassed();
    }

    private boolean firstSurveyDelayPassed() {
        long userAge = new Date().getTime() - createdAt*1000;
        return userAge >= FIRST_SURVEY;
    }

    private boolean dayDelayPassed(){
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        if(!prefs.isLastSeenSet()) {
            return false;
        }

        long timeSinceLastSeen = new Date().getTime() - prefs.getLastSeen();

        return timeSinceLastSeen >= FIRST_SURVEY;
    }

    void checkEligibility() {
        CheckEligibilityTask checkEligibilityTask = new CheckEligibilityTask(
                accountToken, endUserEmail, dailyResponseCap,
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
