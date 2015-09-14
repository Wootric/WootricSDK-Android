package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.network.SurveyClient;
import com.wootric.androidsdk.network.TrackingPixelClient;
import com.wootric.androidsdk.network.WootricApiClient;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by maciejwitowski on 4/11/15.
 */

public class WootricTest {

    @Mock SurveyManager mockSurveyManager;
    @Mock SurveyValidator mockSurveyValidator;

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCOUNT_TOKEN = "account_token";

    @Before
    public void setUp() {
        Wootric.singleton = null;
        Wootric.init(new Activity(), CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
        MockitoAnnotations.initMocks(this);
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

        doReturn(true).when(mockSurveyManager).start();

        assertThat(wootric.surveyInProgress).isFalse();
        wootric.survey();
        assertThat(wootric.surveyInProgress).isTrue();
    }
}
