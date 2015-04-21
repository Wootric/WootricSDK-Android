package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Wootric {

    private final Activity activity;

    static Wootric singleton = null;

    public static Wootric with(Activity activity) {
        if(singleton == null) {
            synchronized (Wootric.class) {
                if(singleton == null) {
                    singleton = new Wootric(activity);
                }
            }
        }

        return singleton;
    }

    public UserManager user(String clientId, String clientSecret, String accountToken) {
        User user = new User(clientId, clientSecret, accountToken);
        return new UserManager(activity, user);
    }

    private Wootric(Activity activity) {
        if(activity == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        this.activity = activity;
    }
}
