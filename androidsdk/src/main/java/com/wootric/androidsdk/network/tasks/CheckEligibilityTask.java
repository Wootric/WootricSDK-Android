/*
 * Copyright (c) 2016 Wootric (https://wootric.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.wootric.androidsdk.network.tasks;

import android.util.Log;

import com.wootric.androidsdk.Constants;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maciejwitowski on 10/13/15.
 */
public class CheckEligibilityTask extends WootricRemoteRequestTask {

    private final User user;
    private final EndUser endUser;
    private final Settings settings;
    private final PreferencesUtils preferencesUtils;

    private final Callback surveyCallback;

    public CheckEligibilityTask(User user, EndUser endUser, Settings settings, PreferencesUtils preferencesUtils, Callback surveyCallback) {
        super(REQUEST_TYPE_GET, null, null);

        this.user = user;
        this.endUser = endUser;
        this.settings = settings;
        this.preferencesUtils = preferencesUtils;
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
        addOptionalParam("external_id", endUser.getExternalId());
        addOptionalParam("phone_number", endUser.getPhoneNumber());
        addOptionalParam("first_survey_delay", settings.getFirstSurveyDelay());
        addOptionalParam("daily_response_cap", settings.getDailyResponseCap());
        addOptionalParam("registered_percent", settings.getRegisteredPercent());
        addOptionalParam("visitor_percent", settings.getVisitorPercent());
        addOptionalParam("resurvey_throttle", settings.getResurveyThrottle());
        addOptionalParam("language[code]", settings.getLanguageCode());
        addOptionalParam("language[product_name]", settings.getProductName());
        addOptionalParam("language[audience_text]", settings.getRecommendTarget());
        addOptionalParam("end_user_last_seen", preferencesUtils.getLastSeen() / 1000);

        Log.d(Constants.TAG, "parameters: " + paramsMap);
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
                    Log.d(Constants.TAG, "Server says the user is eligible for survey");

                    JSONObject settingsObject = jsonObject.getJSONObject("settings");
                    settings = Settings.fromJson(settingsObject);
                }
                else {
                    Log.d(Constants.TAG, "Server says the user is NOT eligible for survey");
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