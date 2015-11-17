package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricCustomMessage;
import com.wootric.androidsdk.objects.WootricCustomThankYou;
import com.wootric.androidsdk.utils.PermissionsValidator;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Wootric {

    final WeakReference<Activity> weakActivity;
    final WeakReference<Context> weakContext;

    final EndUser endUser;
    final User user;
    final Settings settings;

    boolean surveyInProgress;
    PreferencesUtils preferencesUtils;
    PermissionsValidator permissionsValidator;

    static Wootric singleton;

    public static synchronized Wootric init(Activity activity, String clientId, String clientSecret, String accountToken) {
        if (singleton == null) {
            if (activity == null) {
                throw new IllegalArgumentException("Activity must not be null.");
            }

            if (clientId == null || clientSecret == null || accountToken == null) {
                throw new IllegalArgumentException("Client Id, Client Secret and Account token must not be null");
            }

            singleton = new Wootric(activity, clientId, clientSecret, accountToken);
        }

        return singleton;
    }

    public static Wootric get() {
        return singleton;
    }

    public static void notifySurveyFinished() {
        if (singleton != null) {
            singleton.setSurveyFinished();
        }
    }

    private void setSurveyFinished() {
        preferencesUtils.touchLastSurveyed();
        surveyInProgress = false;
        singleton = null;
    }

    public void setEndUserEmail(String email) {
        endUser.setEmail(email);
    }

    public void setEndUserCreatedAt(long createdAt) {
        endUser.setCreatedAt(createdAt);
    }

    public void setProperties(HashMap<String, String> properties) {
        endUser.setProperties(properties);
    }

    public void setSurveyImmediately(boolean surveyImmediately) {
        this.settings.setSurveyImmediately(surveyImmediately);
    }

    public void setCustomMessage(WootricCustomMessage customMessage) {
        settings.setLocalCustomMessage(customMessage);
    }

    public void setDailyResponseCap(int value) {
        settings.setDailyResponseCap(value);
    }

    public void setRegisteredPercent(int value) {
        settings.setRegisteredPercent(value);
    }

    public void setVisitorPercent(int value) {
        settings.setVisitorPercent(value);
    }

    public void setResurveyThrottle(int value) {
        settings.setResurveyThrottle(value);
    }

    public void setLanguageCode(String languageCode) {
        settings.setLanguageCode(languageCode);
    }

    public void setProductName(String productName) {
        settings.setProductName(productName);
    }

    public void setRecommendTarget(String recommendTarget) {
        settings.setRecommendTarget(recommendTarget);
    }

    public void setFacebookPageId(String facebookPage) {
        settings.setFacebookPageId(facebookPage);
    }

    public void setTwitterPage(String tweeterPage) {
        settings.setTwitterPage(tweeterPage);
    }

    public void setCustomThankYou(WootricCustomThankYou customThankYou) {
        settings.setCustomThankYou(customThankYou);
    }

    public void survey() {
        if (!permissionsValidator.check() || surveyInProgress)
            return;

        getSurveyManager().start();

        surveyInProgress = true;
    }

    private SurveyManager getSurveyManager() {
        OfflineDataHandler offlineDataHandler = new OfflineDataHandler(preferencesUtils);
        WootricRemoteClient wootricRemoteClient = new WootricRemoteClient(offlineDataHandler);

        SurveyValidator surveyValidator = buildSurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        return buildSurveyManager(weakActivity.get(), wootricRemoteClient,
                user, endUser, settings, preferencesUtils, surveyValidator);
    }


    private Wootric(Activity activity, String clientId, String clientSecret, String accountToken) {
        weakActivity = new WeakReference<>(activity);
        weakContext = new WeakReference<>(activity.getApplicationContext());

        endUser = new EndUser();
        user = new User(clientId, clientSecret, accountToken);
        settings = new Settings();

        preferencesUtils = new PreferencesUtils(weakContext);
        permissionsValidator = new PermissionsValidator(weakContext);
    }

    SurveyValidator buildSurveyValidator(User user, EndUser endUser, Settings settings,
                                         WootricRemoteClient wootricRemoteClient, PreferencesUtils preferencesUtils) {
        return new SurveyValidator(user, endUser, settings, wootricRemoteClient, preferencesUtils);
    }

    SurveyManager buildSurveyManager(Activity activity, WootricRemoteClient wootricApiClient, User user,
                                     EndUser endUser, Settings settings, PreferencesUtils preferencesUtils,
                                     SurveyValidator surveyValidator) {
        return new SurveyManager(activity, wootricApiClient, user, endUser,
                settings, preferencesUtils, surveyValidator);
    }
}