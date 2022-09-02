package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.OfflineDataHandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import static org.assertj.core.api.Assertions.assertThat;

import android.util.Log;

@RunWith(RobolectricTestRunner.class)
public class CreateResponseTaskTest {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String UNIQUE_LINK = "12345qwerty";
    private static final String ORIGIN_URL = "com.test.app";
    private static final String TEXT = "Test comment";
    private static final String LANGUAGE = "es-mx";
    private static final long END_USER_ID = 123;
    private static final long USER_ID = 1234;
    private static final long ACCOUNT_ID = 100;
    private static final int PRIORITY = 0;
    private static final int SCORE = 5;

    @Mock OfflineDataHandler offlineDataHandler;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        CreateResponseTask asyncTask = new CreateResponseTask(END_USER_ID, USER_ID, ACCOUNT_ID, ORIGIN_URL, SCORE, PRIORITY, TEXT, ACCESS_TOKEN, "NPS-EU", offlineDataHandler, UNIQUE_LINK, LANGUAGE);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.eu/api/v1/end_users/" + END_USER_ID + "/responses");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        CreateResponseTask asyncTask = new CreateResponseTask(END_USER_ID, USER_ID, ACCOUNT_ID, ORIGIN_URL, SCORE, PRIORITY, TEXT, ACCESS_TOKEN, "NPS", offlineDataHandler, UNIQUE_LINK, LANGUAGE);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users/" + END_USER_ID + "/responses");
    }

    @Test
    public void testPost_RequestWithLanguage() throws Exception {
        CreateResponseTask asyncTask = new CreateResponseTask(END_USER_ID, USER_ID, ACCOUNT_ID, ORIGIN_URL, SCORE, PRIORITY, TEXT, ACCESS_TOKEN, "NPS", offlineDataHandler, UNIQUE_LINK, LANGUAGE);
        asyncTask.buildParams();
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users/" + END_USER_ID + "/responses");
        assertThat(asyncTask.paramsMap.get("survey[language]")).isEqualTo("es-mx");
    }

    @Test
    public void testPost_RequestWithoutLanguage() throws Exception {
        CreateResponseTask asyncTask = new CreateResponseTask(END_USER_ID, USER_ID, ACCOUNT_ID, ORIGIN_URL, SCORE, PRIORITY, TEXT, ACCESS_TOKEN, "NPS", offlineDataHandler, UNIQUE_LINK, null);
        asyncTask.buildParams();
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users/" + END_USER_ID + "/responses");
        assertThat(asyncTask.paramsMap.containsKey("survey[language]")).isFalse();
    }
}
