package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.network.WootricApiCallback;
import com.wootric.androidsdk.objects.EndUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static com.wootric.androidsdk.TestHelper.testEndUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class UpdateEndUserTaskTest {
    private static final String ACCESS_TOKEN = "accessToken";

    @Mock
    WootricApiCallback wootricApiCallback;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        EndUser endUser = testEndUser();
        endUser.setId(4321);
        UpdateEndUserTask asyncTask = new UpdateEndUserTask(endUser, ACCESS_TOKEN, "NPS-EU", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.eu/api/v1/end_users/4321");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        EndUser endUser = testEndUser();
        endUser.setId(4321);
        UpdateEndUserTask asyncTask = new UpdateEndUserTask(endUser, ACCESS_TOKEN, "NPS", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users/4321");
    }
}
