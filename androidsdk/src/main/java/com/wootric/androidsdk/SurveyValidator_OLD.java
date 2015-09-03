package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.wootric.androidsdk.Constants.NOT_SET;

/**
 * Validates if a survey should be shown to end user
 * Created by maciejwitowski on 4/12/15.
 */
public class SurveyValidator_OLD {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    private final User user;
    private final EndUser endUser;

    // Optional
    boolean surveyImmediately   = false;
    boolean forceSurvey         = false;
    int dailyResponseCap        = NOT_SET;
    int registeredPercent       = NOT_SET;
    int visitorPercent          = NOT_SET;
    int resurveyThrottle        = NOT_SET;

    private final ConnectionUtils connectionUtils;
    private final PreferencesUtils preferencesUtils;

    private static final int FIRST_SURVEY = 31*60*60*24*1000; // 31 days

    SurveyValidator_OLD(User user, EndUser endUser,
                        ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils) {
        this.user = user;
        this.endUser = endUser;
        this.connectionUtils = connectionUtils;
        this.preferencesUtils = preferencesUtils;
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


    public void forceSurvey() {
        this.forceSurvey = true;
    }

    public void setOnSurveyValidatedListener(OnSurveyValidatedListener onSurveyValidatedListener) {
        this.onSurveyValidatedListener = onSurveyValidatedListener;
    }

    void validate() {
        if(forceSurvey) {
            notifyShouldShowSurvey(null);
        }

        if(needsSurvey()) {
            if(surveyImmediately) {
                notifyShouldShowSurvey(null);
            } else {
                checkEligibility();
            }
        }
    }

    boolean needsSurvey() {
        boolean wasRecentlySurveyed = preferencesUtils.wasRecentlySurveyed();

        if(wasRecentlySurveyed) {
            return false;
        }

            return surveyImmediately ||
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
//        CheckEligibilityTask checkEligibilityTask = new CheckEligibilityTask(
//                user.getAccountToken(), endUser, dailyResponseCap,
//                registeredPercent, visitorPercent, resurveyThrottle, connectionUtils) {
//            @Override
//            protected void onPostExecute(JSONObject response) {
//                if(response != null) {
//                    parseEligibilityResponse(response);
//                }
//            }
//        };
//
//        checkEligibilityTask.execute();
    }

    private void parseEligibilityResponse(JSONObject response) {
        try {
            boolean eligible = response.getBoolean("eligible");

            if(eligible) {
                JSONObject settingsJson = response.getJSONObject("settings");
                if(settingsJson != null) {
                    Settings settings = Settings.fromJson(settingsJson);

                    notifyShouldShowSurvey(settings);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
