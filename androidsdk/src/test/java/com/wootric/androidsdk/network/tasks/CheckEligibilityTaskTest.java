package com.wootric.androidsdk.network.tasks;

import com.wootric.androidsdk.SurveyValidator;
import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static com.wootric.androidsdk.TestHelper.testUser;
import static com.wootric.androidsdk.TestHelper.testPreferenceUtils;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class CheckEligibilityTaskTest {
    @Mock WootricRemoteClient wootricRemoteClient;

    @Test
    public void testGet_RequestWithProperties() throws Exception {
        EndUser endUser = new EndUser();
        Settings settings = new Settings();
        HashMap<String, String> properties = new HashMap<>();
        SurveyValidator surveyValidator = new SurveyValidator(testUser(), endUser, settings,
                wootricRemoteClient, testPreferenceUtils());

        properties.put("pricing_plan", "test plan");
        endUser.setProperties(properties);

        CheckEligibilityTask asyncTask = new CheckEligibilityTask(testUser(), endUser, settings, testPreferenceUtils(), surveyValidator);
        asyncTask.execute();
        Robolectric.flushBackgroundThreadScheduler();

        assertTrue(asyncTask.paramsMap.containsKey("properties[pricing_plan]"));
    }
}
