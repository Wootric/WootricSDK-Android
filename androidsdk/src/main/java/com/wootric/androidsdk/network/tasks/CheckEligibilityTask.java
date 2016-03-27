package com.wootric.androidsdk.network.tasks;

import android.util.Log;

import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CheckEligibilityTask extends WootricRemoteRequestTask {

    private final User user;
    private final EndUser endUser;
    private final Settings settings;

    private final Callback surveyCallback;

    private static final String TAG = "WOOTRIC_SDK";

    public CheckEligibilityTask(User user, EndUser endUser, Settings settings, Callback surveyCallback) {
        super(REQUEST_TYPE_GET, null, null);

        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.surveyCallback = surveyCallback;
    }

    @Override
    protected void buildParams() {
        paramsMap.put("account_token", user.getAccountToken());

        String email = endUser.getEmail();
        // If email is not set, we send an empty string in order to be consistent with web beacon
        if(email == null) {
            email = "";
        }

        paramsMap.put("email", email);

        paramsMap.put("survey_immediately", String.valueOf(settings.isSurveyImmediately()));

        addOptionalParam("end_user_created_at", endUser.getCreatedAtOrNull());
        addOptionalParam("first_survey_delay", settings.getFirstSurveyDelay());
        addOptionalParam("daily_response_cap", settings.getDailyResponseCap());
        addOptionalParam("registered_percent", settings.getRegisteredPercent());
        addOptionalParam("visitor_percent", settings.getVisitorPercent());
        addOptionalParam("resurvey_throttle", settings.getResurveyThrottle());
        addOptionalParam("language[code]", settings.getLanguageCode());
        addOptionalParam("language[product_name]", settings.getProductName());
        addOptionalParam("language[audience_text]", settings.getRecommendTarget());

        Log.v(TAG, "parameters: " + paramsMap);
    }

    @Override
    protected String requestUrl() {
        return ELIGIBLE_URL;
    }

    @Override
    protected void onSuccess(String response) {
        boolean eligible = false;
        Settings settings = null;

        if(response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                eligible = jsonObject.getBoolean("eligible");
                if (eligible) {
                    Log.v(TAG, "Server says the user is eligible for survey");

                    JSONObject settingsObject = jsonObject.getJSONObject("settings");
                    settings = Settings.fromJson(settingsObject);
                }
                else {
                    Log.v(TAG, "Server says the user is NOT eligible for survey");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        EligibilityResponse eligibilityResponse = new EligibilityResponse(eligible, settings);
        surveyCallback.onEligibilityChecked(eligibilityResponse);
    }

    public interface Callback {
        void onEligibilityChecked(EligibilityResponse eligibilityResponse);
    }
}