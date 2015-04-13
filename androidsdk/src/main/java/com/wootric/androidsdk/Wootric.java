package com.wootric.androidsdk;

import android.content.Context;

/**
 * Created by maciejwitowski on 4/10/15.
 */
public class Wootric {

    final Context context;

    static Wootric singleton = null;

    public static Wootric with(Context context) {
        if(singleton == null) {
            synchronized (Wootric.class) {
                if(singleton == null) {
                    singleton = new Wootric(context);
                }
            }
        }

        return singleton;
    }

    public UserManager user(String clientId, String clientSecret, String accountToken) {
        return new UserManager(context, clientId, clientSecret, accountToken);
    }

    private Wootric(Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        this.context = context;
    }
}
