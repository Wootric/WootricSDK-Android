package com.wootric.androidsdk;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Constants {

    static final String ELIGIBILITY_URL =
            "http://wootric-eligibility.herokuapp.com/eligible.json";

    static final int NOT_SET = -1;

    // Request params
    static final String PARAM_ACCOUNT_TOKEN         = "account_token";
    static final String PARAM_EMAIL                 = "email";
    static final String PARAM_DAILY_RESPONSE_CAP    = "daily_response_cap";
    static final String PARAM_REGISTERED_PERCENT    = "registered_percent";
    static final String PARAM_VISITOR_PERCENT       = "visitor_percent";
    static final String PARAM_RESURVEY_THROTTLE     = "resurvey_throttle";
}
