package com.wootric.androidsdk;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricEvent;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.wootric.androidsdk.TestHelper.TEST_FRAGMENT_ACTIVITY;
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

    @Mock
    WootricSurveyCallback surveyCallback;

    @Before
    public void setUp() {
        SurveyManager.sharedInstance = null;
    }

    @Test
    public void updatesLastSeen() throws Exception {
        SurveyManager surveyManager = SurveyManager.getSharedInstance();

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        verify(preferencesUtils, times(1)).touchLastSeen();
    }

    @Test
    public void validatesSurvey() throws Exception {
        SurveyManager surveyManager = SurveyManager.getSharedInstance();

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(1)).validate();
    }

    @Test
    public void setsOnSurveyValidatedListener() throws Exception {
        SurveyManager surveyManager = SurveyManager.getSharedInstance();

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(1)).setOnSurveyValidatedListener(surveyManager);
    }

    @Test
    public void sendsGetAccessTokenRequest() throws Exception {
        SurveyManager surveyManager = SurveyManager.getSharedInstance();
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        surveyManager.onSurveyValidated(new Settings());

        WootricEvent event = surveyManager.getCurrentEvent();
        verify(wootricApiClient, times(1)).authenticate(event.getUser(), surveyManager);
    }

    @Test
    public void sendsGetEndUserRequest() throws Exception {
        EndUser endUser = new EndUser("nps@example.com");
        SurveyManager surveyManager = SurveyManager.getSharedInstance();
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        final String accessToken = "test123test";
        surveyManager.onAuthenticateSuccess(accessToken);

        verify(wootricApiClient, times(1)).getEndUserByEmail(endUser.getEmailOrUnknown(), accessToken, surveyManager);
    }

    @Test
    public void whenEndUserReceived_showsSurvey() throws Exception {
        long receivedId = 1;
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        WootricEvent event = surveyManager.getCurrentEvent();
        EndUser endUser = event.getEndUser();
        assertThat(endUser.getId()).isEqualTo(receivedId);
        verify(surveyManager, times(1)).showSurvey();
    }

    @Test
    public void whenEndUserReceived_sendsRequestToUpdateEndUserProperties() throws Exception {
        long receivedId = 1;
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        WootricEvent event = surveyManager.getCurrentEvent();
        EndUser endUser = event.getEndUser();
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
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        WootricEvent event = surveyManager.getCurrentEvent();
        EndUser endUser = event.getEndUser();
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
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        doNothing().when(surveyManager).showSurvey();

        surveyManager.onGetEndUserIdSuccess(receivedId);

        WootricEvent event = surveyManager.getCurrentEvent();
        EndUser endUser = event.getEndUser();
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
        SurveyManager surveyManager = SurveyManager.getSharedInstance();
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        final String accessToken = "test123test";
        surveyManager.setAccessToken(accessToken);

        surveyManager.onEndUserNotFound();

        WootricEvent event = surveyManager.getCurrentEvent();
        verify(wootricApiClient, times(1)).createEndUser(event.getEndUser(), accessToken, surveyManager);
    }

    @Test
    public void showSurvey() throws Exception {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        doNothing().when(surveyManager).showSurvey();

        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        surveyManager.onCreateEndUserSuccess(receivedEndUser.getId());

        assertThat(surveyManager.getCurrentEvent().getEndUser().getId()).isEqualTo(receivedEndUser.getId());
        verify(surveyManager, times(1)).showSurvey();
    }

    @Test
    public void showSurveyWithActivity() throws Exception {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        doNothing().when(surveyManager).showSurvey();

        EndUser receivedEndUser = new EndUser();
        receivedEndUser.setId(1);
        surveyManager.onCreateEndUserSuccess(receivedEndUser.getId());

        WootricEvent event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getId()).isEqualTo(receivedEndUser.getId());
        verify(surveyManager, times(1)).showSurvey();
    }

    @Test
    public void surveyCallbackWillShow() throws Exception {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        surveyManager.showSurvey();

        verify(surveyCallback, times(1)).onSurveyWillShow();
    }

    @Test
    public void whenStartIsCalledMultipleTimes_selectsCorrectEvent() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        EndUser endUser1 = new EndUser("testOne@wootric.com");
        EndUser endUser2 = new EndUser("testTwo@wootric.com");
        EndUser endUser3 = new EndUser("testThree@wootric.com");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser1, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser2, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser3, new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        WootricEvent event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser1.getEmail());
    }

    @Test
    public void iteratesThroughQueue() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        EndUser endUser1 = new EndUser("testOne@wootric.com");
        EndUser endUser2 = new EndUser("testTwo@wootric.com");
        EndUser endUser3 = new EndUser("testThree@wootric.com");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser1, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser2, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser3, new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        WootricEvent event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser1.getEmail());

        surveyManager.onSurveyNotNeeded();
        event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser2.getEmail());

        surveyManager.onSurveyNotNeeded();
        event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser3.getEmail());
    }

    @Test
    public void clearsQueueWhenEndUserSurveyed() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        EndUser endUser1 = new EndUser("testOne@wootric.com");
        EndUser endUser2 = new EndUser("testTwo@wootric.com");
        EndUser endUser3 = new EndUser("testThree@wootric.com");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser1, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser2, new Settings(), preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser3, new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        WootricEvent event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser1.getEmail());

        surveyManager.onSurveyValidated(new Settings());
        event = surveyManager.getCurrentEvent();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser1.getEmail());
        assertThat(surveyManager.eventQueueCount()).isEqualTo(0);
    }

    @Test
    public void whenEventNamePresent_validateIfEventInRegisteredEvents() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        ArrayList<String> registeredEvents = new ArrayList<>();
        registeredEvents.add("On Purchase");
        registeredEvents.add("On Exit");

        EndUser endUser = new EndUser("testOne@wootric.com");
        Settings settings = new Settings();
        settings.setEventName("On Exit");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser, settings, preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.onRegisteredEvents(registeredEvents);

        verify(surveyValidator, times(1)).validate();
    }

    @Test
    public void whenEventNamePresent_doesntValidateIfEventNotInRegisteredEvents() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        ArrayList<String> registeredEvents = new ArrayList<>();
        registeredEvents.add("On Purchase");
        registeredEvents.add("On Exit");
        surveyManager.onRegisteredEvents(registeredEvents);

        EndUser endUser = new EndUser("testOne@wootric.com");
        Settings settings = new Settings();
        settings.setEventName("On Click");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser, settings, preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(0)).validate();
    }

    @Test
    public void whenEventNamePresent_callsValidateForCorrectEvent() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        ArrayList<String> registeredEvents = new ArrayList<>();
        registeredEvents.add("On Purchase");
        registeredEvents.add("On Exit");
        surveyManager.onRegisteredEvents(registeredEvents);

        EndUser endUser = new EndUser("testOne@wootric.com");
        Settings settings = new Settings();
        settings.setEventName("On Click");

        EndUser endUser2 = new EndUser("testTwo@wootric.com");
        Settings settings2 = new Settings();
        settings2.setEventName("On Exit");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser, settings, preferencesUtils, surveyCallback, surveyValidator);
        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), endUser2, settings2, preferencesUtils, surveyCallback, surveyValidator);

        WootricEvent event = surveyManager.getCurrentEvent();
        verify(surveyValidator, times(1)).validate();
        assertThat(event.getEndUser().getEmail()).isEqualTo(endUser2.getEmail());
    }

    @Test
    public void whenEventNamePresent_callsRegisteredEventsIfListIsEmpty() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());

        Settings settings = new Settings();
        settings.setEventName("On Click");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), settings, preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(1)).getRegisteredEvents();
    }

    @Test
    public void whenEventNamePresent_doesntCallRegisteredEventsIfListIsNotEmpty() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());
        ArrayList<String> registeredEvents = new ArrayList<>();
        registeredEvents.add("On Purchase");
        registeredEvents.add("On Exit");
        surveyManager.onRegisteredEvents(registeredEvents);

        Settings settings = new Settings();
        settings.setEventName("On Click");

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), settings, preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(0)).getRegisteredEvents();
    }
    @Test
    public void whenEventNameNotPresent_doesntCallRegisteredEvents() {
        SurveyManager surveyManager = spy(SurveyManager.getSharedInstance());

        surveyManager.start(TEST_FRAGMENT_ACTIVITY,
                wootricApiClient, testUser(), testEndUser(), new Settings(), preferencesUtils, surveyCallback, surveyValidator);

        verify(surveyValidator, times(0)).getRegisteredEvents();
    }
}
