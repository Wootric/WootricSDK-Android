package com.wootric.androidsdk.tasks;

import com.wootric.androidsdk.TestUtils;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.HashMap;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.TEST_ACCESS_TOKEN;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CreateEndUserTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;
    @Mock
    TestUtils.TestOnEndUserCreatedListener testOnEndUserCreatedListener;

    CreateEndUserTask createEndUserTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendsCreateEndUserRequest() throws Exception {
        EndUser endUser = new EndUser(END_USER_EMAIL);

        HashMap<String, String> properties = new HashMap<>();
        properties.put("prop1", "value1");

        endUser.setProperties(properties);

        long createdAt = new Date().getTime();

        endUser.setCreatedAt(createdAt);

        createEndUserTask = new CreateEndUserTask(endUser, TEST_ACCESS_TOKEN,
                testOnEndUserCreatedListener, connectionUtilsMock);

        String url = "https://api.wootric.com/v1/end_users?email=nps%40example.com&" +
                "external_created_at=" + String.valueOf(createdAt) + "&properties%5Bprop1%5D=value1";

        createEndUserTask.execute();

        verify(connectionUtilsMock).sendAuthorizedPost(url, TEST_ACCESS_TOKEN);
    }

}
