package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class SurveyManager implements SurveyValidator.OnSurveyValidatedListener {

    private final Activity context;
    private final SurveyValidator surveyValidator;

    // Mandatory
    private final String endUserEmail;
    private final String originUrl;

    SurveyManager(Activity context, SurveyValidator surveyValidator,
                  String endUserEmail, String originUrl) {

        if(context == null || surveyValidator == null || endUserEmail == null || originUrl == null) {
            throw new IllegalArgumentException
                    ("Mandatory params cannot be null.");
        }

        this.context = context;
        this.surveyValidator = surveyValidator;
        this.endUserEmail = endUserEmail;
        this.originUrl = originUrl;
    }

    public SurveyManager surveyImmediately() {
        surveyValidator.setSurveyImmediately(true);
        return this;
    }

    public SurveyManager createdAt(long createdAt) {
        surveyValidator.setCreatedAt(createdAt);
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

    public void survey() {
        updateLastSeen();

        surveyValidator.setOnSurveyValidatedListener(this);
        surveyValidator.validate();
    }

    private void updateLastSeen() {
        PreferencesUtils prefs = PreferencesUtils.getInstance(context);

        if(!prefs.wasRecentlyLastSeen()) {
            prefs.setLastSeenNow();
        }
    }

    @Override
    public void onSurveyValidated(boolean shouldShowSurvey) {
        if(shouldShowSurvey) {
            Intent surveyActivity = new Intent(context, SurveyActivity.class);
            context.startActivity(surveyActivity);
            context.overridePendingTransition(0,0);
        }
    }
}
