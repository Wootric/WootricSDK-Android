package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static com.wootric.androidsdk.TestHelper.ORIGIN_URL;
import static com.wootric.androidsdk.TestHelper.TEST_ACTIVITY;
import static com.wootric.androidsdk.TestHelper.testEndUser;
import static com.wootric.androidsdk.TestHelper.testUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SurveyManagerTest {

    @Mock
    WootricRemoteClient wootricApiClient;

    @Mock
    PreferencesUtils preferencesUtils;

    @Mock
    SurveyValidator surveyValidator;

    @Test
    public void sendsGetTrackingPixelRequest() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = new SurveyManager(TEST_ACTIVITY,
                wootricApiClient, user, endUser, new Settings(), preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(wootricApiClient, times(1)).getTrackingPixel(user, endUser, ORIGIN_URL);
    }

    @Test
    public void updatesLastSeen() throws Exception {
        SurveyManager surveyManager = new SurveyManager(TEST_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(preferencesUtils, times(1)).touchLastSeen();
    }

    @Test
    public void validatesSurvey() throws Exception {
        SurveyManager surveyManager = new SurveyManager(TEST_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(surveyValidator, times(1)).validate();
    }

    @Test
    public void setsOnSurveyValidatedListener() throws Exception {
        SurveyManager surveyManager = new SurveyManager(TEST_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(surveyValidator, times(1)).setOnSurveyValidatedListener(surveyManager);
    }

    @Test
    public void sendsGetAccessTokenRequest() throws Exception {
        User user = testUser();
        Settings settings = new Settings();
        SurveyManager surveyManager = new SurveyManager(new Activity(),
                wootricApiClient, user, testEndUser(), settings, preferencesUtils,
                surveyValidator);

        surveyManager.onSurveyValidated(new Settings());

        verify(wootricApiClient, times(1)).authenticate(user, surveyManager);
    }

    @Test
    public void sendsGetEndUserRequest() throws Exception {
        EndUser endUser = new EndUser("nps@example.com");
        SurveyManager surveyManager = new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator);

        final String accessToken = "test123test";
        surveyManager.onAuthenticateSuccess(accessToken);

        verify(wootricApiClient, times(1)).getEndUserByEmail(endUser.getEmailOrUnknown(), accessToken, surveyManager);
    }

    @Test
    public void whenEndUserReceived_showsSurvey() throws Exception {
        long receivedId = 1;

        EndUser endUser = testEndUser();
        SurveyManager surveyManager = spy(new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator));

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        assertThat(endUser.getId()).isEqualTo(receivedId);
        verify(surveyManager, times(1)).showSurvey();
    }

    @Test
    public void whenEndUserReceived_sendsRequestToUpdateEndUserProperties() throws Exception {
        long receivedId = 1;

        EndUser endUser = testEndUser();

        SurveyManager surveyManager = spy(new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator));

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        HashMap<String, String> endUserProperties = new HashMap<>();
        endUserProperties.put("company", "wootric");
        endUser.setProperties(endUserProperties);
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(1)).updateEndUser(endUser, accessToken, surveyManager);
    }

    @Test
    public void whenEndUserReceived_sendsRequestToUpdateEndUserPhoneNumber() throws Exception {
        long receivedId = 1;

        EndUser endUser = testEndUser();

        SurveyManager surveyManager = spy(new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator));

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setPhoneNumber(null);
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setPhoneNumber("");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setPhoneNumber("     ");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setPhoneNumber("+0123456789");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(1)).updateEndUser(endUser, accessToken, surveyManager);
    }

    @Test
    public void whenEndUserReceived_sendsRequestToUpdateEndUserExternalId() throws Exception {
        long receivedId = 1;

        EndUser endUser = testEndUser();

        SurveyManager surveyManager = spy(new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator));

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setExternalId(null);
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setExternalId("");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setExternalId("     ");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(0)).updateEndUser(endUser, accessToken, surveyManager);

        endUser.setExternalId("a1b2c3d4");
        surveyManager.onGetEndUserIdSuccess(receivedId);
        verify(wootricApiClient, times(1)).updateEndUser(endUser, accessToken, surveyManager);
    }

    @Test
    public void whenEndUserNotFound_sendsRequestToCreateEndUser() throws Exception {
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator);
        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        surveyManager.onEndUserNotFound();

        verify(wootricApiClient, times(1)).createEndUser(endUser, accessToken, surveyManager);
    }

    @Test
    public void showSurvey() throws Exception {
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = spy(new SurveyManager(new Activity(),
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils,
                surveyValidator));

        doNothing().when(surveyManager).showSurvey();

        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        surveyManager.onCreateEndUserSuccess(receivedEndUser.getId());

        assertThat(endUser.getId()).isEqualTo(receivedEndUser.getId());
        verify(surveyManager, times(1)).showSurvey();
    }
}
