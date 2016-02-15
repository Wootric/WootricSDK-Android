package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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

import static com.wootric.androidsdk.utils.Utils.checkNotNull;

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

    static volatile Wootric singleton;

    private static final String TAG = "WOOTRIC_SDK";

    public static  Wootric init(Activity activity, String clientId, String clientSecret, String accountToken) {
        Wootric local = singleton;
        if(local == null) {
            synchronized (Wootric.class) {
                local = singleton;
                if(local == null) {
                    checkNotNull(activity, "Activity");
                    checkNotNull(clientId, "Client Id");
                    checkNotNull(clientSecret, "Client Secret");
                    checkNotNull(accountToken, "Account Token");
                    singleton = local = new Wootric(activity, clientId, clientSecret, accountToken);
                }
            }
        }

        return local;
    }

    public static void notifySurveyFinished(boolean surveyShown) {
        if(singleton == null) return;

        if(surveyShown) {
            singleton.preferencesUtils.touchLastSurveyed();
        }

        singleton = null;
    }

    // For tests and integrations
    public Wootric get() {
        return singleton;
    }

    public void setEndUserEmail(String email) {
        endUser.setEmail(email);
    }

    /**
     * Set the end user created date. The createdAt  must be a seconds unix time value and
     * it must be 10 digits only.
     * Important: If you use System.currentTimeMillis() make sure you divide it by 1000.
     * @param createdAt
     */
    public void setEndUserCreatedAt(long createdAt) {

        if (createdAt > 9999999999L){
            Log.v(TAG, "WARNING: The created date exceeds the maximum 10 characters allowed. " +
                    "If you are using System.currentTimeMillis() divide it by 1000.");
        }
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

    public void shouldSkipFollowupScreenForPromoters(boolean shouldSkipFollowupScreenForPromoters) {
        settings.setSkipFollowupScreenForPromoters(shouldSkipFollowupScreenForPromoters);
    }

    public void survey() {
        if (!permissionsValidator.check() || surveyInProgress) {
            return;
        }

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