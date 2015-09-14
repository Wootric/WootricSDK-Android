package com.wootric.androidsdk;

import com.wootric.androidsdk.network.SurveyClient;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.util.Date;

import static com.wootric.androidsdk.Constants.NOT_SET;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyValidator implements SurveyClient.SurveyCallback {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    final User user;
    final EndUser endUser;
    final Settings settings;
    final SurveyClient surveyClient;
    final PreferencesUtils preferencesUtils;

    private static final int FIRST_SURVEY = 31*60*60*24*1000; // 31 days

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

        if(wasRecentlySurveyed)
            return false;

        return settings.isSurveyImmediately() ||
                endUser.getCreatedAt() == NOT_SET ||
                firstSurveyDelayPassed() ||
                dayDelayPassed();
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

    void checkEligibility() {
        surveyClient.checkEligibility(user, endUser, settings, this);
    }

    @Override
    public void onEligibilityChecked(EligibilityResponse eligibilityResponse) {
        if(eligibilityResponse.isEligible()) {
            notifyShouldShowSurvey(eligibilityResponse.getSettings());
        }
    }

    void notifyShouldShowSurvey(Settings settings) {
        if(onSurveyValidatedListener != null) {
            onSurveyValidatedListener.onSurveyValidated(settings);
        }
    }

    interface OnSurveyValidatedListener {
        void onSurveyValidated(Settings settings);
    }
}
