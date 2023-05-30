package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.network.WootricApiCallback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class GetAccessTokenTaskTest {
    @Mock WootricApiCallback wootricApiCallback;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        GetAccessTokenTask asyncTask = new GetAccessTokenTask("clientId1234", "NPS-EU-1234abcd", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.eu/oauth/token");
    }

    @Test
    public void testGet_RequestWithAuToken() throws Exception {
        GetAccessTokenTask asyncTask = new GetAccessTokenTask("clientId1234", "NPS-AU-1234abcd", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.au/oauth/token");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        GetAccessTokenTask asyncTask = new GetAccessTokenTask("clientId1234", "NPS", wootricApiCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://app.wootric.com/oauth/token");
    }
}
