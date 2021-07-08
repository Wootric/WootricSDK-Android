package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.network.WootricApiCallback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class CreateEndUserTaskTest {
    private static final String ACCESS_TOKEN = "accessToken";

    @Mock WootricApiCallback wootricApiCallback;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        CreateEndUserTask asyncTask = new CreateEndUserTask(null, ACCESS_TOKEN, "NPS-EU", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.eu/api/v1/end_users");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        CreateEndUserTask asyncTask = new CreateEndUserTask(null, ACCESS_TOKEN, "NPS", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://api.wootric.com/api/v1/end_users");
    }
}
