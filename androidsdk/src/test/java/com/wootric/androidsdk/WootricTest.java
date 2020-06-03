package com.wootric.androidsdk;

import android.content.res.Resources;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.objects.WootricCustomMessage;
import com.wootric.androidsdk.utils.PermissionsValidator;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static com.wootric.androidsdk.TestHelper.TEST_FRAGMENT_ACTIVITY;
import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WootricTest {

    @Mock SurveyManager mockSurveyManager = SurveyManager.getSharedInstance();
    @Mock SurveyValidator mockSurveyValidator;
    @Mock PreferencesUtils mockPreferencesUtils;
    @Mock PermissionsValidator mockPermissionsValidator;
    @Mock Resources mResources;
    @Mock FragmentActivity mFragmentActivity;
    @Mock Window mWindow;

    private static final String CLIENT_ID = "client_id";
    private static final String ACCOUNT_TOKEN = "account_token";

    @Before
    public void setUp() {
        Wootric.singleton = null;

        Mockito.when(mResources.getBoolean(R.bool.isTablet)).thenReturn(true);
        Mockito.when(mFragmentActivity.getResources()).thenReturn(mResources);
        Mockito.when(mFragmentActivity.getWindow()).thenReturn(mWindow);
        Wootric.init(mFragmentActivity, ACCOUNT_TOKEN);
    }

    @Test public void fails_whenContextIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(null, ACCOUNT_TOKEN);
            Wootric.init(null, CLIENT_ID, ACCOUNT_TOKEN);
            fail("Null activity should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void fails_whenAccountTokenIsNull() throws Exception {
        try {
            Wootric.singleton = null;
            Wootric.init(new FragmentActivity(), CLIENT_ID, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test public void inits_singleton() throws Exception {
        Wootric wootric = Wootric.init(TEST_FRAGMENT_ACTIVITY, CLIENT_ID, ACCOUNT_TOKEN);
        Wootric wootric_1 = Wootric.init(TEST_FRAGMENT_ACTIVITY, CLIENT_ID, ACCOUNT_TOKEN);
        Wootric wootric_2 = Wootric.init(TEST_FRAGMENT_ACTIVITY, CLIENT_ID, ACCOUNT_TOKEN);
        Wootric wootric_3 = Wootric.init(TEST_FRAGMENT_ACTIVITY, CLIENT_ID, ACCOUNT_TOKEN);

        assertThat(wootric).isEqualTo(wootric_1);
        assertThat(wootric_2).isEqualTo(wootric_3);
    }

    @Test public void init_sets_endUser() throws Exception {
        assertThat(Wootric.singleton.endUser).isNotNull();
    }

    @Test public void init_sets_user() throws Exception {
        final User user = Wootric.singleton.user;
        assertThat(user.getAccountToken()).isEqualTo(ACCOUNT_TOKEN);
    }

    @Test public void init_sets_Settings() throws Exception {
        assertThat(Wootric.singleton.settings).isNotNull();
    }

    @Test public void setEndUserEmail() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserEmail("nps@example.com");

        assertThat(wootric.endUser.getEmailOrUnknown()).isEqualTo("nps@example.com");
    }

    @Test public void setEndUserExternalId() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserExternalId("a1b2c3d4");

        assertThat(wootric.endUser.getExternalId()).isEqualTo("a1b2c3d4");
    }

    @Test public void setEndUserPhoneNumber() throws Exception {
        Wootric wootric = Wootric.singleton;
        wootric.setEndUserPhoneNumber("+0123456789");

        assertThat(wootric.endUser.getPhoneNumber()).isEqualTo("+0123456789");
    }

    @Test
    public void setsEndUserProperties() {
        Wootric wootric = Wootric.singleton;
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("company", "Wootric");
        properties.put("type", "free");
        wootric.setProperties(properties);

        assertThat(wootric.endUser.getProperties()).isEqualTo(properties);
    }

    @Test public void setSurveyImmediately() throws Exception {
        Wootric wootric = Wootric.init(new FragmentActivity(), CLIENT_ID, ACCOUNT_TOKEN);
        wootric.setSurveyImmediately(true);

        assertThat(wootric.settings.isSurveyImmediately()).isTrue();
    }

    @Test
    public void setsLocalCustomMessageInSettings() {
        WootricCustomMessage customMessage = new WootricCustomMessage();
        Wootric wootric = Wootric.singleton;

        wootric.setCustomMessage(customMessage);
        assertThat(wootric.settings.getLocalCustomMessage()).isEqualTo(customMessage);
    }

    @Test
    public void setsDailyResponseCapInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setDailyResponseCap(20);
        assertThat(wootric.settings.getDailyResponseCap()).isEqualTo(20);
    }

    @Test
    public void setsRegisteredPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRegisteredPercent(20);
        assertThat(wootric.settings.getRegisteredPercent()).isEqualTo(20);
    }

    @Test
    public void setsVisitorPercentInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setVisitorPercent(20);
        assertThat(wootric.settings.getVisitorPercent()).isEqualTo(20);
    }

    @Test
    public void setsResurveyThrottleInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setResurveyThrottle(20);
        assertThat(wootric.settings.getResurveyThrottle()).isEqualTo(20);
    }

    @Test
    public void setsLanguageCodeInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setLanguageCode("PL");
        assertThat(wootric.settings.getLanguageCode()).isEqualTo("PL");
    }

    @Test
    public void setsProductNameInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setProductName("Wootric");
        assertThat(wootric.settings.getProductName()).isEqualTo("Wootric");
    }

    @Test
    public void setsRecommendTargetInSettings() {
        Wootric wootric = Wootric.singleton;
        wootric.setRecommendTarget("My Friend");
        assertThat(wootric.settings.getRecommendTarget()).isEqualTo("My Friend");
    }

    @Test
    public void setsFacebookPage() {
        Wootric wootric = Wootric.singleton;
        wootric.setFacebookPageId("https://www.facebook.com/test");
        assertThat(wootric.settings.getFacebookPageId()).isEqualTo("https://www.facebook.com/test");
    }

    @Test
    public void setsTweeterPage() {
        Wootric wootric = Wootric.singleton;
        wootric.setTwitterPage("https://www.tweeter.com/test");
        assertThat(wootric.settings.getTwitterPage()).isEqualTo("https://www.tweeter.com/test");
    }

    @Test public void survey_startsSurvey() throws Exception {
        Wootric wootric = spy(Wootric.init(TEST_FRAGMENT_ACTIVITY, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator();
        doReturn(mockSurveyManager).when(wootric).buildSurveyManager();

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();

        wootric.survey();
        verify(mockSurveyManager, times(1)).start(eq(wootric.weakFragmentActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), any(WootricSurveyCallback.class), eq(mockSurveyValidator));
    }

    @Test public void showSurveyInActivity_startsSurvey() throws Exception {
        Wootric wootric = spy(Wootric.init(TEST_FRAGMENT_ACTIVITY, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator();
        doReturn(mockSurveyManager).when(wootric).buildSurveyManager();

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(true).when(wootric.permissionsValidator).check();

        wootric.showSurveyInActivity(TEST_FRAGMENT_ACTIVITY);
        verify(mockSurveyManager, times(1)).start(eq(wootric.weakFragmentActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), any(WootricSurveyCallback.class), eq(mockSurveyValidator));
    }

    @Test
    public void doesNotStartSurvey_whenPermissionsValidatorChecksReturnsFalse() {
        Wootric wootric = spy(Wootric.init(TEST_FRAGMENT_ACTIVITY, ACCOUNT_TOKEN));

        doReturn(mockSurveyValidator).when(wootric).buildSurveyValidator();

        doReturn(mockSurveyManager).when(wootric).buildSurveyManager();

        wootric.permissionsValidator = mockPermissionsValidator;
        doReturn(false).when(wootric.permissionsValidator).check();

        wootric.survey();

        verify(mockSurveyManager, times(0)).start(eq(wootric.weakFragmentActivity.get()),
                any(WootricRemoteClient.class), eq(wootric.user),
                eq(wootric.endUser), eq(wootric.settings),
                any(PreferencesUtils.class), any(WootricSurveyCallback.class), eq(mockSurveyValidator));
    }

    @Test
    public void whenEndUserWasSurveyed_updatesLastSurveyedAndResetsTheSingleton() {
        Wootric wootric = Wootric.singleton;
        wootric.preferencesUtils = mockPreferencesUtils;

        Wootric.notifySurveyFinished(true, true, 0);
        assertThat(Wootric.singleton).isNull();
        verify(wootric.preferencesUtils, times(1)).touchLastSurveyed(true, 0);
    }

    @Test
    public void whenEndUserWasNotSurveyed_doesNotpdateLastSurveyed() {
        Wootric wootric = Wootric.singleton;
        wootric.preferencesUtils = mockPreferencesUtils;

        Wootric.notifySurveyFinished(false, false, 0);
        assertThat(Wootric.singleton).isNull();
        verify(wootric.preferencesUtils, never()).touchLastSurveyed(false, 0);
    }

    @Test
    public void whenSkipFeedbackScreenTrue_doesNotShowFeedbackScreen() {
        Wootric wootric = spy(Wootric.init(TEST_FRAGMENT_ACTIVITY, ACCOUNT_TOKEN));
        wootric.skipFeedbackScreen(true);
        assertThat(wootric.settings.skipFeedbackScreen()).isTrue();
    }
}
