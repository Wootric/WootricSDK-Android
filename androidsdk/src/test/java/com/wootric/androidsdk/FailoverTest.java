package com.wootric.androidsdk;

import android.app.Activity;
import android.util.Log;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.tasks.CreateResponseTask;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.URLEncoder;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.ORIGIN_URL;
import static com.wootric.androidsdk.TestUtils.TEST_ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Created by Tommy on 5/29/15.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class FailoverTest {

    private static final int TEST_SCORE = 1;
    private static final String TEST_TEXT = "testText";

    @Mock
    ConnectionUtils mockConnectionUtil;

    ConnectionUtils realConnectionUtil;
    Activity activity;
    EndUser endUser;
    PreferencesUtils prefs;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        realConnectionUtil = ConnectionUtils.get();

        activity = TestUtils.testActivity();
        endUser = new EndUser(1, END_USER_EMAIL);

        prefs = PreferencesUtils.getInstance(activity);
    }

    @After
    public void tearDown() {
        prefs.clear();
    }

    @Test
    public void storeResponseWithoutConnection() throws Exception {

        // simulate failed request without text
        CreateResponseTask createResponseTask = new CreateResponseTask(TEST_ACCESS_TOKEN, endUser, ORIGIN_URL,
                TEST_SCORE, "", realConnectionUtil, prefs);
        createResponseTask.execute();
        assertThat(prefs.getUnsentResponse()).isNotNull();

        Log.v("message", prefs.getUnsentResponse());
        prefs.clear();

        // simulate failed request with text
        CreateResponseTask createResponseWithTextTask = new CreateResponseTask(TEST_ACCESS_TOKEN, endUser, ORIGIN_URL,
                TEST_SCORE, TEST_TEXT, realConnectionUtil, prefs);
        createResponseWithTextTask.execute();
        assertThat(prefs.getUnsentResponse()).isNotNull();

        Log.v("message", prefs.getUnsentResponse());
        prefs.clear();
    }
}
