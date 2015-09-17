package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.EligibilityResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by maciejwitowski on 9/15/15.
 */
interface SurveyInterface {
    @GET("//eligible.json")
    void eligible(@Query("account_token") String accountToken,
                  @Query("email") String email,
                  @Query("end_user_created_at") Long createdAtOrNull,
                  @Query("survey_immediately") boolean surveyImmediately,
                  @Query("first_survey_delay") long firstSurveyDelay,
                  @Query("daily_response_cap") Integer dailyResponseCap,
                  @Query("registered_percent") Integer registeredPercent,
                  @Query("visitor_percent") Integer visitorPercent,
                  @Query("resurvey_throttle") Integer resurveyThrottle,
                  @Query("language[code]") String code,
                  @Query("language[product_name]") String productName,
                  @Query("language[audience_text") String audienceText,
                  Callback<EligibilityResponse> eligibilityResponseCallback);
}