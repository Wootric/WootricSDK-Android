package com.wootric.androidsdk;

import android.content.Intent;

import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.robolectric.Shadows.shadowOf;

import static com.wootric.androidsdk.TestUtils.*;


/**
 * Created by maciejwitowski on 4/11/15.
*/
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SurveyManagerTest {

    private SurveyManager surveyManager;
    private SurveyValidator surveyValidator;

    @Before
    public void setup() {
        surveyValidator = new SurveyValidator(testActivity(),
                TestUtils.ACCOUNT_TOKEN, TestUtils.END_USER_EMAIL);

        surveyManager = new SurveyManager(testActivity(), surveyValidator, TestUtils.END_USER_EMAIL,
                TestUtils.ORIGIN_URL);
    }

    @Test
    public void failsWhenConstructorArgumentsAreInvalid() throws Exception {
        try {
            new SurveyManager(null, surveyValidator, TestUtils.END_USER_EMAIL, TestUtils.ACCOUNT_TOKEN);
            fail("Null context should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager(testActivity(), null, TestUtils.END_USER_EMAIL, TestUtils.ACCOUNT_TOKEN);
            fail("Null survey validator email should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager(testActivity(), surveyValidator, null, TestUtils.ACCOUNT_TOKEN);
            fail("Null end user email should throw exception");
        } catch (IllegalArgumentException expected) {
        }

        try {
            new SurveyManager(testActivity(), surveyValidator, TestUtils.END_USER_EMAIL, null);
            fail("Null account token should throw exception");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void startsSurveyActivityAfterSurveyValidationSucceeded() throws Exception {
        surveyManager.onSurveyValidated();
        Intent expectedIntent = new Intent(testActivity(), SurveyActivity.class);
        assertThat(shadowOf(testActivity()).getNextStartedActivity()).isEqualTo(expectedIntent);
    }

    @Test
    public void surveyUpdatesLastSeenIfNotSet() {
        SurveyManager mockManager = spy(surveyManager);
        Mockito.doNothing().when(mockManager).setupSurveyValidator();
        mockManager.survey();

        assertThat(sharedPrefs().getLastSeen()).isNotEqualTo(-1);
    }

    @Test
    public void surveyUpdatesLastSeenIfNotRecent() {
        // Set old last seen
        long oldLastSeen = new Date().getTime() - 91*1000*60*60*24;
        PreferencesUtils.getInstance(testActivity()).setLastSeen(oldLastSeen);

        // Mock setupSurveyValidator method
        SurveyManager mockManager = spy(surveyManager);
        Mockito.doNothing().when(mockManager).setupSurveyValidator();

        // Start survey
        mockManager.survey();

        // Check new last seen
        long newLastSeen = sharedPrefs().getLastSeen();
        assertThat(newLastSeen).isNotEqualTo(-1);
        assertThat(newLastSeen).isNotEqualTo(oldLastSeen);
    }

    @Test
    public void surveyDoesNotUpdateLastSeenIfRecent() {
        long recentLastSeen = new Date().getTime() - 89*1000*60*60*24;
        sharedPrefs().setLastSeen(recentLastSeen);

        SurveyManager mockManager = spy(surveyManager);
        Mockito.doNothing().when(mockManager).setupSurveyValidator();
        mockManager.survey();

        assertThat(sharedPrefs().getLastSeen()).isEqualTo(recentLastSeen);
    }

    @Test
    public void surveyStartsValidator() {

    }
}