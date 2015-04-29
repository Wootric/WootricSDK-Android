package com.wootric.androidsdk.tasks;

import com.wootric.androidsdk.TestUtils;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.ConnectionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.testUser;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/29/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class GetAccessTokenTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;
    @Mock
    TestUtils.TestOnAccessTokenReceivedListener testOnAccessTokenReceivedListener;

    GetAccessTokenTask getAccessTokenTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendsCreateEndUserRequest() throws Exception {
        final User user = testUser();
        getAccessTokenTask = new GetAccessTokenTask(user,
                testOnAccessTokenReceivedListener, connectionUtilsMock);

        String url = "https://api.wootric.com/oauth/token?grant_type=client_credentials&" +
                "client_id=" + user.getClientId() + "&client_secret=" + user.getClientSecret();

        getAccessTokenTask.execute();

        verify(connectionUtilsMock).sendPost(url);
    }

}
