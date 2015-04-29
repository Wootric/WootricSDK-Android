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

import static com.wootric.androidsdk.TestUtils.ACCOUNT_TOKEN;
import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/13/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class CheckEligibilityTaskTest {

    @Mock ConnectionUtils connectionUtilsMock;

    CheckEligibilityTask eligibilityTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        EndUser endUser = new EndUser(1, END_USER_EMAIL);
        eligibilityTask = new CheckEligibilityTask(ACCOUNT_TOKEN, endUser, 100,100,100,100, connectionUtilsMock);
    }

    @Test
    public void sendsEligibilityRequest() throws Exception {
        String url = "http://wootric-eligibility.herokuapp.com/eligible.json?" +
                "account_token=testAccountToken&email=nps%40example.com&daily_response_cap=100&" +
                "registered_percent=100&visitor_percent=100&resurvey_throttle=100";

        eligibilityTask.execute();

        verify(connectionUtilsMock).sendGet(url);
    }
}
