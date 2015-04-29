package com.wootric.androidsdk.utils;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Constants {

    public static final String ELIGIBILITY_URL =
            "http://wootric-eligibility.herokuapp.com/eligible.json";

    public static final String API_URL = "https://api.wootric.com/v1/";
    public static final String END_USERS_URL = API_URL + "end_users";

    public static final String TRACKING_PIXEL_URL = "https://d8myem934l1zi.cloudfront.net/pixel.gif?";

    public static final String WOOTRIC_URL = "https://www.wootric.com";


    public static final int INVALID_ID = -1;

    public static final int NOT_SET = -1;

    // Request params
    public static final String PARAM_ACCOUNT_TOKEN         = "account_token";
    public static final String PARAM_EMAIL                 = "email";
    public static final String PARAM_DAILY_RESPONSE_CAP    = "daily_response_cap";
    public static final String PARAM_REGISTERED_PERCENT    = "registered_percent";
    public static final String PARAM_VISITOR_PERCENT       = "visitor_percent";
    public static final String PARAM_RESURVEY_THROTTLE     = "resurvey_throttle";

    public static final String PARAM_RESPONSE_ORIGIN_URL   = "origin_url";
    public static final String PARAM_RESPONSE_SCORE        = "score";
    public static final String PARAM_RESPONSE_TEXT         = "text";
}
