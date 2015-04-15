package com.wootric.androidsdk;

import android.app.Activity;

import org.junit.Test;
import org.robolectric.Robolectric;

/**
 * Created by maciejwitowski on 4/11/15.
 */
public class TestUtils {

    static final String CLIENT_ID = "test client id";
    static final String CLIENT_SECRET = "test client secret";
    static final String ACCOUNT_TOKEN = "test account token";
    static final String END_USER_EMAIL = "nps@example.com";
    static final String ORIGIN_URL = "http://example.com";

    private static TestActivity TEST_ACTVITY;

    static TestActivity testActivity() {
        if(TEST_ACTVITY == null) {
            TEST_ACTVITY = Robolectric.buildActivity(TestActivity.class).create().get();
        }

        return TEST_ACTVITY;
    }

    static PreferencesUtils sharedPrefs() {
        return PreferencesUtils.getInstance(testActivity());
    }
}
