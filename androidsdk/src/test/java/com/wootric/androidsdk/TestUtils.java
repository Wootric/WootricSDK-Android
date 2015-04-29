package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.tasks.CreateEndUserTask;
import com.wootric.androidsdk.tasks.GetAccessTokenTask;
import com.wootric.androidsdk.tasks.GetEndUserTask;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.robolectric.Robolectric;

/**
 * Created by maciejwitowski on 4/11/15.
 */
public class TestUtils {

    public static final String CLIENT_ID = "testClientId";
    public static final String CLIENT_SECRET = "testClientSecret";
    public static final String ACCOUNT_TOKEN = "testAccountToken";
    public static final String END_USER_EMAIL = "nps@example.com";
    public static final String ORIGIN_URL = "http://example.com";
    public static final String TEST_ACCESS_TOKEN = "123abc";

    private static TestActivity TEST_ACTVITY;
    private static User TEST_USER;

    public static TestActivity testActivity() {
        if(TEST_ACTVITY == null) {
            TEST_ACTVITY = Robolectric.buildActivity(TestActivity.class).create().get();
        }

        return TEST_ACTVITY;
    }

    public static PreferencesUtils sharedPrefs() {
        return PreferencesUtils.getInstance(testActivity());
    }

    public static User testUser() {
        if(TEST_USER == null) {
            TEST_USER = new User(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        }
        return TEST_USER;
    }

    public static class TestOnEndUserCreatedListener implements CreateEndUserTask.OnEndUserCreatedListener {

        @Override
        public void onEndUserCreated(EndUser endUser) {

        }
    }

    public static class TestOnEndUserReceivedListener implements GetEndUserTask.OnEndUserReceivedListener {

        @Override
        public void onEndUserReceived(EndUser endUser) {

        }
    }

    public static class TestOnAccessTokenReceivedListener implements GetAccessTokenTask.OnAccessTokenReceivedListener {

        @Override
        public void onAccessTokenReceived(String accessToken) {

        }
    }

}
