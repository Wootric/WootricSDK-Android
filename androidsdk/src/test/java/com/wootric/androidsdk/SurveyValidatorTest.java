package com.wootric.androidsdk;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.testUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by maciejwitowski on 4/13/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SurveyValidatorTest {

    private SurveyValidator surveyValidator;

    @Mock ConnectionUtils connectionUtils;
    @Mock PreferencesUtils preferencesUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        surveyValidator = new SurveyValidator(
                testUser(),
                new EndUser(1, END_USER_EMAIL),
                connectionUtils,
                preferencesUtils);
    }

    @Test
    public void doesNotNeedSurvey_whenSurveyedRecently() throws Exception {
        when(preferencesUtils.wasRecentlySurveyed()).thenReturn(true);

        SurveyValidator.OnSurveyValidatedListener listener = mock(SurveyValidator.OnSurveyValidatedListener.class);

        surveyValidator.setOnSurveyValidatedListener(listener);
        surveyValidator.setSurveyImmediately(false);
        surveyValidator.validate();

        verify(listener, never()).onSurveyValidated();
    }

    @Test
    public void showsSurvey_whenNotSurveyedRecentlyAndSurveyImmediatelyIsSet() throws Exception {
        when(preferencesUtils.wasRecentlySurveyed()).thenReturn(false);

        SurveyValidator.OnSurveyValidatedListener listener = mock(SurveyValidator.OnSurveyValidatedListener.class);

        surveyValidator.setOnSurveyValidatedListener(listener);
        surveyValidator.setSurveyImmediately(true);
        surveyValidator.validate();

        verify(listener, times(1)).onSurveyValidated();
    }

    @Test
    public void setters_setProperties() throws Exception {
        surveyValidator.setSurveyImmediately(true);
        assertThat(surveyValidator.surveyImmediately).isTrue();

        surveyValidator.setDailyResponseCap(100);
        assertThat(surveyValidator.dailyResponseCap).isEqualTo(100);

        surveyValidator.setRegisteredPercent(100);
        assertThat(surveyValidator.registeredPercent).isEqualTo(100);

        surveyValidator.setResurveyThrottle(100);
        assertThat(surveyValidator.resurveyThrottle).isEqualTo(100);

        surveyValidator.setVisitorPercent(100);
        assertThat(surveyValidator.visitorPercent).isEqualTo(100);
    }


}
