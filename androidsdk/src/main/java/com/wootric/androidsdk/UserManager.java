package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class UserManager {

    private final WeakReference<Activity> weakActivity;
    private final User user;

    UserManager(Activity activity, User user) {
        if(activity == null || user == null) {
            throw new IllegalArgumentException("Activity and user must not be null.");
        }

        this.weakActivity = new WeakReference<Activity>(activity);
        this.user = user;
    }

    public SurveyManager endUser(String endUserEmail, String originUrl) {
        EndUser endUser = new EndUser(endUserEmail);
        SurveyValidator surveyValidator = new SurveyValidator(weakActivity, user, endUser);
        return new SurveyManager(weakActivity, user, endUser, surveyValidator, originUrl);
    }

    public SurveyManager endUser(String endUserEmail, String originUrl, HashMap properties) {
        EndUser endUser = new EndUser(endUserEmail, properties);
        SurveyValidator surveyValidator = new SurveyValidator(weakActivity, user, endUser);
        return new SurveyManager(weakActivity, user, endUser, surveyValidator, originUrl);
    }
}
