package com.wootric.androidsdk.network.tasks;
import com.wootric.androidsdk.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)

public class GetRegisteredEventsTaskTest {
    @Mock GetRegisteredEventsTask.Callback surveyCallback;

    @Test
    public void testGet_RequestWithEuToken() throws Exception {
        User user = new User("NPS-EU");

        GetRegisteredEventsTask asyncTask = new GetRegisteredEventsTask(user, surveyCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://eligibility.wootric.eu/registered_events.json");
    }

    @Test
    public void testGet_RequestWithNormalToken() throws Exception {
        User user = new User("NPS");

        GetRegisteredEventsTask asyncTask = new GetRegisteredEventsTask(user, surveyCallback);
        assertThat(asyncTask.requestUrl()).isEqualTo("https://survey.wootric.com/registered_events.json");
    }
}
