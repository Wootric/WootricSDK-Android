package com.wootric.androidsdk;

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
}
