package com.wootric.androidsdk.network;

import android.util.Log;

import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by maciejwitowski on 9/14/15.
 */
public class SurveyClient {
    private static final String SURVEY_BASE_URL = "http://wootric-eligibility.herokuapp.com";

    private static final String LOG_TAG = SurveyClient.class.getName();

    private final SurveyInterface surveyInterface;

    public SurveyClient() {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint(SURVEY_BASE_URL)
                .build();

        surveyInterface = retrofit.create(SurveyInterface.class);
    }

    public SurveyClient(SurveyInterface surveyInterface) {
        this.surveyInterface = surveyInterface;
    }

    public void checkEligibility(User user, EndUser endUser, Settings settings, final SurveyCallback surveyCallback) {
        surveyInterface.eligible(
                user.getAccountToken(),
                endUser.getEmail(),
                endUser.getCreatedAtOrNull(),
                settings.isSurveyImmediately(),
                settings.getFirstSurveyDelay(),
                settings.getDailyResponseCap(),
                settings.getRegisteredPercent(),
                settings.getVisitorPercent(),
                settings.getResurveyThrottle(),
                settings.getLanguageCode(),
                settings.getProductName(),
                settings.getRecommendTarget(),
                new Callback<EligibilityResponse>() {

                @Override
                public void success(EligibilityResponse eligibilityResponse, Response response) {
                    surveyCallback.onEligibilityChecked(eligibilityResponse);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(LOG_TAG, "failure: " + error.toString());
                }
            });
    }

    public interface SurveyCallback {
        void onEligibilityChecked(EligibilityResponse eligibilityResponse);
    }
}
