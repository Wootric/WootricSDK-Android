package com.wootric.androidsdk;

import com.wootric.androidsdk.network.SurveyClient;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.util.Date;

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

    private static final long FIRST_SURVEY = 31*86400000; // 31 days

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

    void validate() {
        if(needsSurvey())
            checkEligibility();
    }

    private boolean needsSurvey() {
        boolean wasRecentlySurveyed = preferencesUtils.wasRecentlySurveyed();

        return !wasRecentlySurveyed &&
                (settings.isSurveyImmediately() ||
                !endUser.isCreatedAtSet() ||
                firstSurveyDelayPassed() || dayDelayPassed());
    }

    private boolean firstSurveyDelayPassed() {
        long userAge = new Date().getTime() - endUser.getCreatedAt()*1000;
        return userAge >= FIRST_SURVEY;
    }

    private boolean dayDelayPassed(){
        if(!preferencesUtils.isLastSeenSet()) {
            return false;
        }

        long timeSinceLastSeen = new Date().getTime() - preferencesUtils.getLastSeen();

        return timeSinceLastSeen >= FIRST_SURVEY;
    }

    private void checkEligibility() {
        surveyClient.checkEligibility(user, endUser, settings, this);
    }

    @Override
    public void onEligibilityChecked(EligibilityResponse eligibilityResponse) {
        if(eligibilityResponse.isEligible()) {
            notifyShouldShowSurvey(eligibilityResponse.getSettings());
        }
    }

    private void notifyShouldShowSurvey(Settings settings) {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated(settings);
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated(Settings settings);
    }
}
