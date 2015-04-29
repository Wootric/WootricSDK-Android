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

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.TEST_ACCESS_TOKEN;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CreateDeclineTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;

    CreateDeclineTask createDeclineTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        EndUser endUser = new EndUser(1, END_USER_EMAIL);
        createDeclineTask = new CreateDeclineTask(TEST_ACCESS_TOKEN, endUser, connectionUtilsMock);
    }

    @Test
    public void sendsCreateDeclineRequest() throws Exception {
        String url = "https://api.wootric.com/v1/end_users/1/declines";

        createDeclineTask.execute();

        verify(connectionUtilsMock).sendAuthorizedPost(url, TEST_ACCESS_TOKEN);
    }
}
