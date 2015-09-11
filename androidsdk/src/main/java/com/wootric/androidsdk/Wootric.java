package com.wootric.androidsdk;

import android.content.Context;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.lang.ref.WeakReference;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Wootric {

    final Context context;
    final EndUser endUser;
    final User user;
    final Settings settings;
    String originUrl;

    boolean surveyInProgress;

    static Wootric singleton;

    public static Wootric init(Context context, String clientId, String clientSecret, String accountToken) {
        if(singleton == null) {
            singleton = new Wootric(context, clientId, clientSecret, accountToken);
        }

        return singleton;
    }
    
    public void setEndUserEmail(String email) {
        endUser.setEmail(email);
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.settings.setSurveyImmediately(surveyImmediately);
    }

    public void survey() {
        if(surveyInProgress)
            return;

        ConnectionUtils connectionUtils = ConnectionUtils.get();
        PreferencesUtils preferencesUtils = PreferencesUtils.getInstance(context);
        SurveyValidator surveyValidator = buildSurveyValidator(user, endUser, settings,
                connectionUtils, preferencesUtils);
        SurveyManager surveyManager = buildSurveyManager(context, user, endUser, settings, originUrl,
                connectionUtils, preferencesUtils, surveyValidator);

        boolean started = surveyManager.start();

        if(started)
            surveyInProgress = true;
    }

    private Wootric(Context context, String clientId, String clientSecret, String accountToken) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

        if(clientId == null || clientSecret == null || accountToken == null) {
            throw new IllegalArgumentException("Client Id, Client Secret and Account token must not be null");
        }

        this.context = context;
        endUser = new EndUser();
        user = new User(clientId, clientSecret, accountToken);
        settings = new Settings();
    }

    SurveyValidator buildSurveyValidator(User user, EndUser endUser, Settings settings,
                                         ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils) {
        return new SurveyValidator(user, endUser, settings, connectionUtils, preferencesUtils);
    }

     SurveyManager buildSurveyManager(Context context, User user, EndUser endUser, Settings settings, String originUrl,
                                      ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils,
                                      SurveyValidator surveyValidator) {
        return new SurveyManager(context, user, endUser, settings, originUrl,
                connectionUtils, preferencesUtils, surveyValidator);
    }
}
