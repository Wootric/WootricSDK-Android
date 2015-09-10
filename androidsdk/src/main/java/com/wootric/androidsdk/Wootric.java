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

    private final WeakReference<Context> mWeakContext;
    private final EndUser endUser;
    private final User user;
    private String originUrl;
    private final Settings settings;

    private boolean mSurveyInProgress = false;
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
        if(mSurveyInProgress)
            return;

        ConnectionUtils connectionUtils = ConnectionUtils.get();
        PreferencesUtils preferencesUtils = PreferencesUtils.getInstance(mWeakContext.get());

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings, connectionUtils, preferencesUtils);

        SurveyManager surveyManager = new SurveyManager(mWeakContext, user, endUser, originUrl,
                settings, connectionUtils, preferencesUtils, surveyValidator);

        surveyManager.start();

        mSurveyInProgress = true;
    }

    private Wootric(Context context, String clientId, String clientSecret, String accountToken) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }

        if(clientId == null || clientSecret == null || accountToken == null) {
            throw new IllegalArgumentException("Client Id, Client Secret and Account token must not be null");
        }

        mWeakContext = new WeakReference<Context>(context);
        this.endUser = new EndUser();
        this.user = new User(clientId, clientSecret, accountToken);
        this.settings = new Settings();
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
