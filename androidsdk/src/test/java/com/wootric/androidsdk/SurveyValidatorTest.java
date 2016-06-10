package com.wootric.androidsdk;

import com.wootric.androidsdk.network.WootricRemoteClient;
import com.wootric.androidsdk.network.responses.EligibilityResponse;
import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.Settings;
import com.wootric.androidsdk.objects.User;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static com.wootric.androidsdk.TestHelper.testEndUser;
import static com.wootric.androidsdk.TestHelper.testUser;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by maciejwitowski on 9/14/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class SurveyValidatorTest {

    @Mock
    PreferencesUtils preferencesUtils;

    @Mock
    WootricRemoteClient wootricRemoteClient;

    @Mock
    EndUser endUser;

    @Mock
    SurveyValidator.OnSurveyValidatedListener onSurveyValidatedListener;

    /**
     * validate()
     */

    @Test
    public void checksEligibility_whenNotRecentlySurveyed() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        Settings settings = new Settings();

        doReturn(false).when(preferencesUtils).wasRecentlySurveyed();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(1)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    @Test
    public void doesNotCheckEligibility_whenRecentlySurveyed() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        long createdAtJustNow = new Date().getTime();
        endUser.setCreatedAt(createdAtJustNow);
        
        Settings settings = new Settings();

        doReturn(true).when(preferencesUtils).wasRecentlySurveyed();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(0)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    @Test
    public void checksEligibility_whenSurveyImmediatelyIsSet() throws Exception {
        User user = testUser();

        EndUser endUser = testEndUser();
        long createdAtJustNow = new Date().getTime();
        endUser.setCreatedAt(createdAtJustNow);

        Settings settings = new Settings();
        settings.setSurveyImmediately(true);

        doReturn(false).when(preferencesUtils).wasRecentlySurveyed();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(1)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    @Test
    public void checksEligibility_whenEndUserCreatedAtIsNotSet() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        Settings settings = new Settings();

        doReturn(false).when(preferencesUtils).wasRecentlySurveyed();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(1)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    @Test
    public void checksEligibility_whenFirstSurveyDelayPassed() throws Exception {
        User user = testUser();
        EndUser endUser = testEndUser();
        long timeBeforeFirstSurveyDelay = new Date().getTime() - Constants.DAY_IN_MILLIS * 32;
        endUser.setCreatedAt(timeBeforeFirstSurveyDelay);

        Settings settings = new Settings();

        doReturn(false).when(preferencesUtils).wasRecentlySurveyed();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(1)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    @Test
    public void checksEligibility_whenLastSeenDelayPassed() throws Exception {
        User user = testUser();

        EndUser endUser = testEndUser();
        long timeNow = new Date().getTime();
        endUser.setCreatedAt(timeNow);

        Settings settings = new Settings();

        doReturn(false).when(preferencesUtils).wasRecentlySurveyed();
        doReturn(true).when(preferencesUtils).isLastSeenSet();
        long timeBeforeFirstSurveyDelay = new Date().getTime() - Constants.DAY_IN_MILLIS * 32;
        doReturn(timeBeforeFirstSurveyDelay).when(preferencesUtils).getLastSeen();

        SurveyValidator surveyValidator = new SurveyValidator(user, endUser, settings,
                wootricRemoteClient, preferencesUtils);

        surveyValidator.validate();

        verify(wootricRemoteClient, times(1)).checkEligibility(user, endUser, settings, preferencesUtils, surveyValidator);
    }

    /**
     * onEligibilityChecked(EligibilityResponse eligibilityResponse)
     */
    @Test
    public void notifiesListener_whenEligibleEqualsTrue() throws Exception {
        Settings settings = new Settings();
        SurveyValidator surveyValidator = new SurveyValidator(testUser(), testEndUser(), settings,
                wootricRemoteClient, preferencesUtils);
        EligibilityResponse eligibilityResponse = new EligibilityResponse(true, settings);

        surveyValidator.setOnSurveyValidatedListener(onSurveyValidatedListener);

        surveyValidator.onEligibilityChecked(eligibilityResponse);
        verify(onSurveyValidatedListener, times(1)).onSurveyValidated(settings);
    }

    @Test
    public void doesNotNotifyListener_whenEligibleEqualsFalse() throws Exception {
        Settings settings = new Settings();
        SurveyValidator surveyValidator = new SurveyValidator(testUser(), testEndUser(), settings,
                wootricRemoteClient, preferencesUtils);
        EligibilityResponse eligibilityResponse = new EligibilityResponse(false, settings);

        surveyValidator.setOnSurveyValidatedListener(onSurveyValidatedListener);

        surveyValidator.onEligibilityChecked(eligibilityResponse);
        verify(onSurveyValidatedListener, times(0)).onSurveyValidated(settings);
    }
}
