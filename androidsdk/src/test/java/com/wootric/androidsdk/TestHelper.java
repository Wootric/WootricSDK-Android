package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

/**
 * Created by maciejwitowski on 9/14/15.
 */
public class TestHelper {

    public static final String CLIENT_ID = "testClientId";
    public static final String CLIENT_SECRET = "testClientSecret";
    public static final String ACCOUNT_TOKEN = "testAccountToken";
    public static final String END_USER_EMAIL = "nps@example.com";
    public static final String ORIGIN_URL = "http://example.com";
    public static final String TEST_ACCESS_TOKEN = "123abc";


    private static User TEST_USER;

    public static User testUser() {
        return new User(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    public static EndUser testEndUser() {
        return new EndUser();
    }
}
