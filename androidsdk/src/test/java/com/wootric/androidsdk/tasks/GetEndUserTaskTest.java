package com.wootric.androidsdk.tasks;

import com.wootric.androidsdk.TestUtils;
import com.wootric.androidsdk.utils.ConnectionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.TEST_ACCESS_TOKEN;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class GetEndUserTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;
    @Mock
    TestUtils.TestOnEndUserReceivedListener testOnEndUserReceivedListener;

    GetEndUserTask getEndUserTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendsCreateEndUserRequest() throws Exception {

        getEndUserTask = new GetEndUserTask(END_USER_EMAIL, TEST_ACCESS_TOKEN,
                testOnEndUserReceivedListener, connectionUtilsMock);

        String url = "https://api.wootric.com/v1/end_users?email=nps%40example.com";

        getEndUserTask.execute();

        verify(connectionUtilsMock).sendAuthorizedGet(url, TEST_ACCESS_TOKEN);
    }

}
