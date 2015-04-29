package com.wootric.androidsdk.tasks;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;

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
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CreateResponseTaskTest {

    private static final int TEST_SCORE = 1;
    private static final String TEST_TEXT = "testText";

    @Mock ConnectionUtils connectionUtilsMock;

    CreateResponseTask createResponseTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        EndUser endUser = new EndUser(1, END_USER_EMAIL);
        createResponseTask = new CreateResponseTask(TEST_ACCESS_TOKEN, endUser, ORIGIN_URL,
                TEST_SCORE, TEST_TEXT, connectionUtilsMock);
    }

    @Test
    public void sendCreateResponseRequest() throws Exception {
        String url = "https://api.wootric.com/v1/end_users/1/responses?origin_url=" + URLEncoder.encode(ORIGIN_URL)
                + "&score=" + String.valueOf(TEST_SCORE) + "&text=" + TEST_TEXT;

        createResponseTask.execute();

        verify(connectionUtilsMock).sendAuthorizedPost(url, TEST_ACCESS_TOKEN);
    }
}
