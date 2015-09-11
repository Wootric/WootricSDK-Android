package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.tasks.CheckEligibilityTask;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.wootric.androidsdk.Constants.NOT_SET;

/**
 * Created by maciejwitowski on 9/3/15.
 */
public class SurveyValidator {

    private OnSurveyValidatedListener onSurveyValidatedListener;
    final User user;
    final EndUser endUser;

    final ConnectionUtils connectionUtils;
    final PreferencesUtils preferencesUtils;

    final Settings settings;

    private static final int FIRST_SURVEY = 31*60*60*24*1000; // 31 days

    SurveyValidator(User user, EndUser endUser, Settings settings,
                        ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils) {
        this.user = user;
        this.endUser = endUser;
        this.connectionUtils = connectionUtils;
        this.preferencesUtils = preferencesUtils;
        this.settings = settings;
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
        CheckEligibilityTask checkEligibilityTask = new CheckEligibilityTask(
                user.getAccountToken(),
                endUser.getEmail(),
                endUser.getCreatedAt(),
                settings.isSurveyImmediately(),
                connectionUtils) {

            @Override
            protected void onPostExecute(JSONObject response) {
                if(response != null) {
                    parseEligibilityResponse(response);
                }
            }
        };

        checkEligibilityTask.execute();
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
