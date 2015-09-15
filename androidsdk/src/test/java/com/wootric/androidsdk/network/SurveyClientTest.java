package com.wootric.androidsdk.network;

import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import retrofit.Callback;

import static com.wootric.androidsdk.TestHelper.testUser;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 9/15/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SurveyClientTest {

    @Mock
    SurveyInterface surveyInterface;

    /**
     * checkEligibility(User user, EndUser endUser, Settings settings, final SurveyCallback surveyCallback)
     */
    @Test
    public void sendsCorrectRequest() throws Exception {
        SurveyClient surveyClient = new SurveyClient(surveyInterface);

        User user = testUser();
        final String email = "nps@example.com";
        EndUser endUser = new EndUser(email);
        long endUserCreatedAt = new Date().getTime();
        endUser.setCreatedAt(endUserCreatedAt);
        Settings settings = new Settings();
        settings.setSurveyImmediately(true);
        settings.setFirstSurveyDelay(100L);

        surveyClient.checkEligibility(user, endUser, settings, new MockSurveyCallback());

        verify(surveyInterface, times(1))
                .eligible(eq(user.getAccountToken()), eq(email), eq(true), eq(endUserCreatedAt),
                        eq(100L), any(Callback.class));
    }

    private class MockSurveyCallback implements SurveyClient.SurveyCallback {

        @Override
        public void onEligibilityChecked(EligibilityResponse eligibilityResponse) {

        }
    }
}
