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

import java.util.HashMap;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.TEST_ACCESS_TOKEN;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class UpdateEndUserTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;

    UpdateEndUserTask updateEndUserTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendsUpdateEndUserRequest() throws Exception {
        EndUser endUser = new EndUser(1, END_USER_EMAIL);

        HashMap<String, String> properties = new HashMap<>();
        properties.put("prop1", "value1");

        endUser.setProperties(properties);

        updateEndUserTask = new UpdateEndUserTask(endUser, TEST_ACCESS_TOKEN, connectionUtilsMock);

        updateEndUserTask.execute();

        String url = "https://api.wootric.com/v1/end_users/1?properties%5Bprop1%5D=value1";
        verify(connectionUtilsMock).sendAuthorizedPut(url, TEST_ACCESS_TOKEN);
    }

}
