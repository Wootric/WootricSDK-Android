package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.network.TrackingPixelClient;
import com.wootric.androidsdk.network.WootricApiClient;
import com.wootric.androidsdk.network.responses.AuthenticationResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.wootric.androidsdk.TestHelper.ORIGIN_URL;
import static com.wootric.androidsdk.TestHelper.testEndUser;
import static com.wootric.androidsdk.TestHelper.testUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 9/15/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SurveyManagerTest {

    @Mock
    WootricApiClient wootricApiClient;

    @Mock
    TrackingPixelClient trackingPixelClient;

    @Mock
    PreferencesUtils preferencesUtils;

    @Mock
    SurveyValidator surveyValidator;


    /**
     * start()
     */
    @Test
    public void sendsGetTrackingPixelRequest() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, user, endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(trackingPixelClient, times(1)).getTrackingPixel(user, endUser, ORIGIN_URL);
    }

    @Test
    public void updatesLastSeen() throws Exception {
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), testEndUser(), new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(preferencesUtils, times(1)).touchLastSeen();
    }

    @Test
    public void validatesSurvey() throws Exception {
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), testEndUser(), new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(surveyValidator, times(1)).validate();
    }

    @Test
    public void setsOnSurveyValidatedListener() throws Exception {
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), testEndUser(), new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);

        surveyManager.start();

        verify(surveyValidator, times(1)).setOnSurveyValidatedListener(surveyManager);
    }

    /**
     * onSurveyValidated(Settings surveyServerSettings)
     */
//    @Test
//    public void mergesSettings() throws Exception {
//        Settings settings = new Settings();
//        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
//                trackingPixelClient, testUser(), testEndUser(), settings, ORIGIN_URL, preferencesUtils,
//                surveyValidator);
//
//        Settings surveyServerSettings = new Settings();
//        surveyServerSettings.setCustomMessage(new CustomMessage());
//
//        surveyManager.onSurveyValidated(surveyServerSettings);
//
//        assertThat(settings.getCustomMessage()).isEqualTo(surveyServerSettings.getCustomMessage());
//    }

    @Test
    public void sendsGetAccessTokenRequest() throws Exception {
        User user = testUser();
        Settings settings = new Settings();
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, user, testEndUser(), settings, ORIGIN_URL, preferencesUtils,
                surveyValidator);

        surveyManager.onSurveyValidated(new Settings());

        verify(wootricApiClient, times(1)).authenticate(user, surveyManager);
    }

    /**
     * onAuthenticateSuccess(AuthenticationResponse authenticationResponse)
     */
    @Test
    public void sendsGetEndUserRequest() throws Exception {
        EndUser endUser = new EndUser("nps@example.com");
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);

        final String accessToken = "test123test";
        AuthenticationResponse resp = new AuthenticationResponse(accessToken);

        surveyManager.onAuthenticateSuccess(resp);

        verify(wootricApiClient, times(1)).getEndUserByEmail(endUser.getEmail(), accessToken, surveyManager);
    }

    /**
     * onGetEndUserSuccess(List<EndUser> endUsers)
     */
    @Test
    public void whenEndUserReceived_showsSurvey() throws Exception {
        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        List<EndUser> endUsers = new ArrayList<>();
        endUsers.add(receivedEndUser);

        EndUser endUser = testEndUser();
        SurveyManager surveyManager = spy(new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator));

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserSuccess(endUsers);

        assertThat(endUser.getId()).isEqualTo(receivedEndUser.getId());
        verify(surveyManager, times(1)).showSurvey();
    }

    @Test
    public void whenEndUserReceived_sendsRequestToUpdateEndUserProperties() throws Exception {
        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        List<EndUser> endUsers = new ArrayList<>();
        endUsers.add(receivedEndUser);

        EndUser endUser = testEndUser();
        HashMap endUserProperties = new HashMap();
        endUserProperties.put("company", "wootric");
        endUser.setProperties(endUserProperties);

        SurveyManager surveyManager = spy(new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator));

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserSuccess(endUsers);

        verify(wootricApiClient, times(1)).updateEndUser(endUser, accessToken);
    }

    @Test
    public void whenEndUserNotReceived_sendsRequestToCreateEndUser() throws Exception {
        List<EndUser> endUsers = new ArrayList<>();
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator);
        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        surveyManager.onGetEndUserSuccess(endUsers);

        verify(wootricApiClient, times(1)).createEndUser(endUser, accessToken, surveyManager);
    }

    /**
     * onCreateEndUserSuccess(EndUser endUser)
     */
    @Test
    public void showSurvey() throws Exception {
        EndUser endUser = testEndUser();
        SurveyManager surveyManager = spy(new SurveyManager(new Activity(), wootricApiClient,
                trackingPixelClient, testUser(), endUser, new Settings(), ORIGIN_URL, preferencesUtils,
                surveyValidator));

        doNothing().when(surveyManager).showSurvey();

        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        surveyManager.onCreateEndUserSuccess(receivedEndUser);

        assertThat(endUser.getId()).isEqualTo(receivedEndUser.getId());
        verify(surveyManager, times(1)).showSurvey();
    }
}
