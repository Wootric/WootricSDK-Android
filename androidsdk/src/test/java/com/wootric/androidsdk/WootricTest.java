package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.network.SurveyClient;
import com.wootric.androidsdk.network.TrackingPixelClient;
import com.wootric.androidsdk.network.WootricApiClient;
import com.wootric.androidsdk.objects.CustomMessage;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PermissionsValidator;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 4/11/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class WootricTest {

    @Mock SurveyManager mockSurveyManager;
    @Mock SurveyValidator mockSurveyValidator;
    @Mock PreferencesUtils mockPreferencesUtils;
    @Mock PermissionsValidator mockPermissionsValidator;

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCOUNT_TOKEN = "account_token";

    @Before
    public void setUp() {
        Wootric.singleton = null;
        Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
    }

    @Test public void fails_whenContextIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(null, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
            fail("Null activity should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenClientIdIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new Activity(), null, CLIENT_SECRET, ACCOUNT_TOKEN);
            fail("Null client id should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenClientSecretIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new Activity(), CLIENT_ID, null, ACCOUNT_TOKEN);
            fail("Null client secret should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenAccountTokenIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void inits_singleton() throws Exception {
        Wootric.singleton = null;
        Wootric wootric = Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        Wootric wootric_2 = Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);

        assertThat(wootric).isEqualTo(wootric_2);
    }

    @Test public void init_sets_endUser() throws Exception {
        assertThat(Wootric.singleton.endUser).isNotNull();
    }

    @Test public void init_sets_user() throws Exception {
        final User user = Wootric.singleton.user;
        assertThat(user.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(user.getClientSecret()).isEqualTo(CLIENT_SECRET);
        assertThat(user.getAccountToken()).isEqualTo(ACCOUNT_TOKEN);
    }

    @Test public void init_sets_Settings() throws Exception {
        assertThat(Wootric.singleton.settings).isNotNull();
    }

    @Test public void setEndUserEmail() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserEmail("nps@example.com");

        assertThat(wootric.endUser.getEmail()).isEqualTo("nps@example.com");
    }

    /**
     * setProperties(HashMap<String, String> properties)
     */
    @Test
    public void setsEndUserProperties() {
        Wootric wootric = Wootric.singleton;
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        wootric.setProperties(properties);

        assertThat(wootric.endUser.getProperties()).isEqualTo(properties);
    }

    @Test public void setOriginUrl() throws Exception {
        Wootric wootric = Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setOriginUrl("test.com");

        assertThat(wootric.originUrl).isEqualTo("test.com");
    }

    @Test public void setSurveyImmediately() throws Exception {
        Wootric wootric = Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        wootric.setSurveyImmediately(true);

        assertThat(wootric.settings.isSurveyImmediately()).isTrue();
    }

    /**
     * setCustomMessage(CustomMessage customMessage)
     */
    @Test
    public void setsLocalCustomMessageInSettings() {
        CustomMessage customMessage = new CustomMessage();
        Wootric wootric = Wootric.singleton;

        wootric.setCustomMessage(customMessage);
        assertThat(wootric.settings.getLocalCustomMessage()).isEqualTo(customMessage);
    }

    /**
     * setDailyResponseCap(int value)
     */
    @Test
    public void setsDailyResponseCapInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setDailyResponseCap(20);
        assertThat(wootric.settings.getDailyResponseCap()).isEqualTo(20);
    }

    /**
     * setRegisteredPercent(int value)
     */
    @Test
    public void setsRegisteredPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRegisteredPercent(20);
        assertThat(wootric.settings.getRegisteredPercent()).isEqualTo(20);
    }

    /**
     * setVisitorPercent(int value)
     */
    @Test
    public void setsVisitorPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setVisitorPercent(20);
        assertThat(wootric.settings.getVisitorPercent()).isEqualTo(20);
    }

    /**
     * setResurveyThrottle(int value)
     */
    @Test
    public void setsResurveyThrottleInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setResurveyThrottle(20);
        assertThat(wootric.settings.getResurveyThrottle()).isEqualTo(20);
    }

    /**
     * setLanguageCode(String languageCode)
     */
    @Test
    public void setsLanguageCodeInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setLanguageCode("PL");
        assertThat(wootric.settings.getLanguageCode()).isEqualTo("PL");
    }

    /**
     * setProductName(String productName)
     */
    @Test
    public void setsProductNameInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setProductName("Wootric");
        assertThat(wootric.settings.getProductName()).isEqualTo("Wootric");
    }

    /**
     * setRecommendTarget(String recommendTarget)
     */
    @Test
    public void setsRecommendTargetInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRecommendTarget("My Friend");
        assertThat(wootric.settings.getRecommendTarget()).isEqualTo("My Friend");
    }

    /**
     * survey()
     */

    @Test public void survey_startsSurvey() throws Exception {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));
        wootric.originUrl = "test.com";

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(SurveyClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.context),
                any(WootricApiClient.class), any(TrackingPixelClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), eq(wootric.originUrl),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();

        wootric.survey();
        verify(mockSurveyManager, times(1)).start();
        assertThat(wootric.surveyInProgress).isTrue();
    }

    @Test
    public void doesNotStartSurvey_whenSurveyInProgress() {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));
        wootric.originUrl = "test.com";

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(SurveyClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.context),
                any(WootricApiClient.class), any(TrackingPixelClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), eq(wootric.originUrl),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();
        wootric.surveyInProgress = true;

        wootric.survey();

        verify(mockSurveyManager, times(0)).start();
    }

    @Test
    public void doesNotStartSurvey_whenPermissionsValidatorChecksReturnsFalse() {
        Wootric.singleton = null;
        Wootric wootric = spy(Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN));
        wootric.originUrl = "test.com";

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator(eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), any(SurveyClient.class), any(PreferencesUtils.class));

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager(eq(wootric.context),
                any(WootricApiClient.class), any(TrackingPixelClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings), eq(wootric.originUrl),
                any(PreferencesUtils.class), eq(mockSurveyValidator));

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(false).when(wootric.permissionsValidator).check();
        wootric.surveyInProgress = false;

        wootric.survey();

        verify(mockSurveyManager, times(0)).start();
    }

    /**
     * notifySurveyFinished()
     */
    @Test
    public void setsSingletonSurveyInProgressToFalseAndUpdatesLastSurveyed() {
        Wootric wootric = Wootric.singleton;
        wootric.preferencesUtils = mockPreferencesUtils;
        wootric.surveyInProgress = true;

        Wootric.notifySurveyFinished();

        assertThat(wootric.surveyInProgress).isFalse();
        verify(wootric.preferencesUtils, times(1)).touchLastSurveyed();
    }
}
