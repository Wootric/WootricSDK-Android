package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class UserManager {

    private final WeakReference<Activity> weakActivity;
    private final User user;
    private SurveyManager_OLD surveyManagerOLD;

    private final ConnectionUtils connectionUtils;
    private final PreferencesUtils preferencesUtils;

    UserManager(Activity activity, User user, ConnectionUtils connectionUtils, PreferencesUtils preferencesUtils) {
        this.connectionUtils = connectionUtils;
        this.preferencesUtils = preferencesUtils;
        if(activity == null || user == null) {
            throw new IllegalArgumentException("Activity and user must not be null.");
        }

        this.weakActivity = new WeakReference<Activity>(activity);
        this.user = user;
    }

    public SurveyManager_OLD endUser(String endUserEmail, String originUrl) {
        EndUser endUser = new EndUser(endUserEmail);

        return getSurveyManager(endUser, originUrl);
    }

    public SurveyManager_OLD endUser(String endUserEmail, String originUrl, HashMap properties) {
        EndUser endUser = new EndUser(endUserEmail, properties);

        return getSurveyManager(endUser, originUrl);
    }

    private SurveyManager_OLD getSurveyManager(EndUser endUser, String originUrl) {
        SurveyValidator_OLD surveyValidatorOLD = new SurveyValidator_OLD(user, endUser, connectionUtils, preferencesUtils);
        surveyManagerOLD = new SurveyManager_OLD(weakActivity, user, endUser, surveyValidatorOLD, originUrl, preferencesUtils, connectionUtils);

        return surveyManagerOLD;
    }

    void invalidateActivity() {
        if(surveyManagerOLD != null) {
            surveyManagerOLD.invalidateActivity();
        }
    }
}
