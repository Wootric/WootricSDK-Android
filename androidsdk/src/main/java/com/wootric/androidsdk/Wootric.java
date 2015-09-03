package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Wootric {

    private final EndUser endUser;
    private final User user;
    private final Activity activity;
    private String originUrl;

    private boolean surveyImmediately = false;

    static Wootric singleton;

    public static Wootric init(Activity activity, String clientId, String clientSecret, String accountToken) {
        if(singleton == null) {
            singleton = new Wootric(activity, clientId, clientSecret, accountToken);
        }

        return singleton;
    }
    
    public void setEndUserEmail(String email) {
        endUser.setEmail(email);
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public void setSurveyImmediately(boolean forceSurvey) {
        this.surveyImmediately = forceSurvey;
    }

    public void survey() {
        ConnectionUtils connectionUtils = ConnectionUtils.get();
        PreferencesUtils preferencesUtils = PreferencesUtils.getInstance(activity);
        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, surveyImmediately, connectionUtils, preferencesUtils);
        SurveyManager surveyManager = new SurveyManager(this, connectionUtils, preferencesUtils, surveyValidator);

        surveyManager.start();
    }

    private Wootric(Activity activity, String clientId, String clientSecret, String accountToken) {
        if(activity == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

        if(clientId == null || clientSecret == null || accountToken == null) {
            throw new IllegalArgumentException("Client Id, Client Secret and Account token must not be null");
        }

        this.activity = activity;
        this.endUser = new EndUser();
        this.user = new User(clientId, clientSecret, accountToken);
    }

    EndUser getEndUser() {
        return endUser;
    }

    User getUser() {
        return user;
    }

    String getOriginUrl() {
        return originUrl;
    }
}
