package com.wootric.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.test.mock.MockPackageManager;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 9/14/15.
 */
public class TestHelper {

    private static final String CLIENT_ID = "testClientId";
    private static final String CLIENT_SECRET = "testClientSecret";
    private static final String ACCOUNT_TOKEN = "testAccountToken";
    public static final String ORIGIN_URL = "";

    public static User testUser() {
        return new User(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    public static EndUser testEndUser() {
        return new EndUser();
    }

    public static final Activity TEST_ACTIVITY = new Activity() {
        @Override
        public PackageManager getPackageManager() {
            return TEST_PACKAGE_MANAGER;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return null;
        }

        @Override
        public Context getApplicationContext() {
            return null;
        }
    };

    private static final MockPackageManager TEST_PACKAGE_MANAGER = new MockPackageManager() {
        @Override
        public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
            return null;
        }
    };
}
