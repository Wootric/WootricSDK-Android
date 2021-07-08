package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.OfflineDataHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CreateDeclineTaskTest {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String UNIQUE_LINK = "12345qwerty";
    private static final String ORIGIN_URL = "com.test.app";
    private static final long END_USER_ID = 123;
    private static final long USER_ID = 1234;
    private static final long ACCOUNT_ID = 100;
    private static final int PRIORITY = 0;

    @Mock OfflineDataHandler offlineDataHandler;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        CreateDeclineTask asyncTask = new CreateDeclineTask(END_USER_ID, USER_ID, ACCOUNT_ID, PRIORITY,ORIGIN_URL, ACCESS_TOKEN, "NPS-EU", offlineDataHandler, UNIQUE_LINK);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.eu/api/v1/end_users/" + END_USER_ID + "/declines");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        CreateDeclineTask asyncTask = new CreateDeclineTask(END_USER_ID, USER_ID, ACCOUNT_ID, PRIORITY,ORIGIN_URL, ACCESS_TOKEN, "NPS", offlineDataHandler, UNIQUE_LINK);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users/" + END_USER_ID + "/declines");
    }
}
