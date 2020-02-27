package com.wootric.androidsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.fragment.app.FragmentActivity;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class TestHelper {
    private static final String CLIENT_ID = "testClientId";
    private static final String CLIENT_SECRET = "testClientSecret";
    private static final String ACCOUNT_TOKEN = "testAccountToken";
    public static final String ORIGIN_URL = "com.test.app";

    public static User testUser() {
        return new User(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    public static EndUser testEndUser() {
        return new EndUser();
    }

    public static PreferencesUtils testPreferenceUtils() {
        WeakReference weakContext = new WeakReference<>(TEST_FRAGMENT_ACTIVITY.getApplicationContext());
        return new PreferencesUtils(weakContext);
    }

    public static final FragmentActivity TEST_FRAGMENT_ACTIVITY = new FragmentActivity() {
        @Override
        public PackageManager getPackageManager() {
            return getPackageManager();
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return TEST_APPLICATION_INFO;
        }

        @Override
        public Context getApplicationContext() {
            return null;
        }
    };

    private static final ApplicationInfo TEST_APPLICATION_INFO = new ApplicationInfo() {
        public static final String packageName = "";
    };

    public String loadJSONFromAsset(String fileName) throws IOException {
        String json = null;
        try {
            String path = "src/test/resources/" + fileName;
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
