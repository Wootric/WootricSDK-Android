package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class UserManager {

    private final Context context;
    private final String clientId;
    private final String clientSecret;
    private final String accountToken;

    UserManager(Context context, String clientId, String clientSecret, String accountToken) {
        if(context == null || clientId == null || clientSecret == null || accountToken == null) {
            throw new IllegalArgumentException("Client id, client secret and account token must not be null.");
        }

        this.context = context;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accountToken = accountToken;
    }

    Context getContext() {
        return context;
    }

    String getClientId() {
        return clientId;
    }

    String getClientSecret() {
        return clientSecret;
    }

    String getAccountToken() {
        return accountToken;
    }

    public SurveyManager endUser(String endUserEmail, String originUrl) {
        SurveyValidator surveyValidator = new SurveyValidator(context, accountToken, endUserEmail);
        return new SurveyManager((Activity)context, surveyValidator, endUserEmail, originUrl);
    }
}
