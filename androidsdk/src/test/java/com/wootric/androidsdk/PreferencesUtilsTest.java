package com.wootric.androidsdk;

import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by maciejwitowski on 5/4/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class PreferencesUtilsTest {
    
    PreferencesUtils prefs;

    @Before
    public void setup() {
        prefs = PreferencesUtils.getInstance(TestUtils.testActivity());
    }

    @After
    public void tearDown() {
        prefs.clear();
    }

    // wasRecentlySeen method
    @Test
    public void wasRecentlySeen_returnsFalse_ifWasSeenMoreThan90DaysAgo() throws Exception {
        prefs.setLastSeen(new Date().getTime() - 1000L *60L *60L *24L *92L);

        assertThat(prefs.wasRecentlySeen()).isFalse();
    }

    @Test
    public void wasRecentlySeen_returnsFalse_ifWasNeverSeen() throws Exception {
        assertThat(prefs.wasRecentlySeen()).isFalse();
    }

    @Test
    public void wasRecentlySeen_returnsTrueIfReallyWas() throws Exception {
        prefs.setLastSeen(new Date().getTime() - 1000L * 60L * 60L * 24L * 89L);

        assertThat(prefs.wasRecentlySeen()).isTrue();
    }

    // wasRecentlySurveyed method
    @Test
    public void wasRecentlySurveyed_returnsFalse_ifWasSurveyedMoreThan90DaysAgo() throws Exception {
        prefs.setLastSurveyed(new Date().getTime() - 1000L * 60L * 60L * 24L * 92L);

        assertThat(prefs.wasRecentlySurveyed()).isFalse();
    }

    @Test
    public void wasRecentlySurveyed_returnsFalse_ifWasNeverSurveyed() throws Exception {
        assertThat(prefs.wasRecentlySurveyed()).isFalse();
    }

    @Test
    public void wasRecentlySurveyed_returnsTrueIfReallyWas() throws Exception {
        prefs.setLastSurveyed(new Date().getTime() - 1000L * 60L * 60L * 24L * 89L);

        assertThat(prefs.wasRecentlySurveyed()).isTrue();
    }

    @Test
    public void setsAccessToken() throws Exception {
        prefs.setAccessToken(TestUtils.TEST_ACCESS_TOKEN);

        assertThat(prefs.getAccessToken()).isEqualTo(TestUtils.TEST_ACCESS_TOKEN);
    }

    @Test
    public void clearsAccessToken() throws Exception {
        prefs.setAccessToken(TestUtils.TEST_ACCESS_TOKEN);
        prefs.clearAccessToken();

        assertThat(prefs.getAccessToken()).isNull();
    }

    @Test
    public void setsEndUserId() throws Exception {
        prefs.setEndUserId(1);

        assertThat(prefs.getEndUserId()).isEqualTo(1);
    }

    @Test
    public void clearsEndUserId() throws Exception {
        prefs.setEndUserId(1);
        prefs.clearEndUserId();

        assertThat(prefs.getEndUserId()).isEqualTo(-1);
    }

    @Test
    public void setsSelectedScore() throws Exception {
        prefs.setSelectedScore(1);

        assertThat(prefs.getSelectedScore()).isEqualTo(1);
    }

    @Test
    public void clearsSelectedScore() throws Exception {
        prefs.setSelectedScore(1);
        prefs.clearSelectedScore();

        assertThat(prefs.getSelectedScore()).isEqualTo(-1);
    }

    @Test
    public void setsFeedbackInputValue() throws Exception {
        prefs.setFeedbackInputValue("Feedback");

        assertThat(prefs.getFeedbackInputValue()).isEqualTo("Feedback");
    }

    @Test
    public void clearsFeedbackInputValue() throws Exception {
        prefs.setFeedbackInputValue("Feedback");
        prefs.clearFeedbackInputValue();

        assertThat(prefs.getFeedbackInputValue()).isNull();
    }

    @Test
    public void setsCurrentState() throws Exception {
        prefs.setCurrentState(1);

        assertThat(prefs.getCurrentState()).isEqualTo(1);
    }

    @Test
    public void setsResponseSet() throws Exception {
        prefs.setResponseSent(true);

        assertThat(prefs.getResponseSent()).isTrue();
    }

    @Test
    public void clearsAllValues() throws Exception {
        // Testing just 2 examplary values
        prefs.setAccessToken("Access token");
        prefs.setEndUserId(1);

        prefs.clear();

        assertThat(prefs.getAccessToken()).isNull();
        assertThat(prefs.getEndUserId()).isEqualTo(-1);
    }
}
