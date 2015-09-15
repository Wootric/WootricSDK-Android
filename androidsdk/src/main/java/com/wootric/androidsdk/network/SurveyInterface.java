package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.EligibilityResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/15/15.
 */
public interface SurveyInterface {
    @GET("//eligible.json")
    void eligible(@Query("account_token") String accountToken,
                  @Query("email") String email,
                  @Query("survey_immediately") boolean surveyImmediately,
                  @Query("end_user_created_at") Long endUserCreatedAt,
                  @Query("first_survey_delay") long firstSurveyDelay,
                  Callback<EligibilityResponse> eligibilityResponseCallback);
}