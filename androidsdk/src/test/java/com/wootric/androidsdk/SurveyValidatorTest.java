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
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
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

    private SurveyValidator_OLD surveyValidatorOLD;

    @Mock ConnectionUtils connectionUtils;
    @Mock PreferencesUtils preferencesUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        surveyValidatorOLD = new SurveyValidator_OLD(
                testUser(),
                new EndUser(1, END_USER_EMAIL),
                connectionUtils,
                preferencesUtils);
    }

    @Test
    public void doesNotNeedSurvey_whenSurveyedRecently() throws Exception {
        when(preferencesUtils.wasRecentlySurveyed()).thenReturn(true);

        SurveyValidator_OLD.OnSurveyValidatedListener listener = mock(SurveyValidator_OLD.OnSurveyValidatedListener.class);

        surveyValidatorOLD.setOnSurveyValidatedListener(listener);
        surveyValidatorOLD.setSurveyImmediately(false);
        surveyValidatorOLD.validate();

        verify(listener, never()).onSurveyValidated(null);
    }

    @Test
    public void showsSurvey_whenNotSurveyedRecentlyAndSurveyImmediatelyIsSet() throws Exception {
        when(preferencesUtils.wasRecentlySurveyed()).thenReturn(false);

        SurveyValidator_OLD.OnSurveyValidatedListener listener = mock(SurveyValidator_OLD.OnSurveyValidatedListener.class);

        surveyValidatorOLD.setOnSurveyValidatedListener(listener);
        surveyValidatorOLD.setSurveyImmediately(true);
        surveyValidatorOLD.validate();

        verify(listener, times(1)).onSurveyValidated(null);
    }

    @Test
    public void setters_setProperties() throws Exception {
        surveyValidatorOLD.setSurveyImmediately(true);
        assertThat(surveyValidatorOLD.surveyImmediately).isTrue();

        surveyValidatorOLD.setDailyResponseCap(100);
        assertThat(surveyValidatorOLD.dailyResponseCap).isEqualTo(100);

        surveyValidatorOLD.setRegisteredPercent(100);
        assertThat(surveyValidatorOLD.registeredPercent).isEqualTo(100);

        surveyValidatorOLD.setResurveyThrottle(100);
        assertThat(surveyValidatorOLD.resurveyThrottle).isEqualTo(100);

        surveyValidatorOLD.setVisitorPercent(100);
        assertThat(surveyValidatorOLD.visitorPercent).isEqualTo(100);
    }


}
