package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 9/11/15.
 */
public class TestHelper {

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCOUNT_TOKEN = "account_token";

    public static User getUser() {
        return new User(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    public static EndUser getEndUser() {
        return new EndUser();
    }

    public static Settings getSettings() {
        return new Settings();
    }
}
