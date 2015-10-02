package com.wootric.androidsdk;

import com.wootric.androidsdk.network.SurveyClient;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyValidator implements SurveyClient.SurveyCallback {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    private final User user;
    private final EndUser endUser;
    private final Settings settings;
    private final SurveyClient surveyClient;
    private final PreferencesUtils preferencesUtils;

    SurveyValidator(User user, EndUser endUser, Settings settings,
                        SurveyClient surveyClient, PreferencesUtils preferencesUtils) {
        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.surveyClient = surveyClient;
        this.preferencesUtils = preferencesUtils;
    }

    public void setOnSurveyValidatedListener(OnSurveyValidatedListener onSurveyValidatedListener) {
        this.onSurveyValidatedListener = onSurveyValidatedListener;
    }

    public void validate() {
        if(needsSurvey()) {
            checkEligibility();
        } else {
            notifyShouldNotShowSurvey();
        }
    }

    @Override
    public void onEligibilityChecked(EligibilityResponse eligibilityResponse) {
        if(eligibilityResponse.isEligible()) {
            notifyShouldShowSurvey(eligibilityResponse.getSettings());
        } else {
            notifyShouldNotShowSurvey();
        }
    }

    private boolean needsSurvey() {
        return !preferencesUtils.wasRecentlySurveyed() &&
                (settings.isSurveyImmediately() ||
                !endUser.isCreatedAtSet() ||
                firstSurveyDelayPassed() || lastSeenDelayPassed());
    }

    private boolean firstSurveyDelayPassed() {
        return settings.firstSurveyDelayPassed(endUser.getCreatedAt());
    }

    private boolean lastSeenDelayPassed(){
        return preferencesUtils.isLastSeenSet() &&
                settings.firstSurveyDelayPassed(preferencesUtils.getLastSeen());
    }

    private void checkEligibility() {
        surveyClient.checkEligibility(user, endUser, settings, this);
    }

    private void notifyShouldShowSurvey(Settings settings) {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated(settings);
        }
    }

    private void notifyShouldNotShowSurvey() {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyNotNeeded();
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated(Settings settings);
        void onSurveyNotNeeded();
    }
}
