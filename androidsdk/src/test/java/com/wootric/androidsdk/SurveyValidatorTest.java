package com.wootric.androidsdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAndroidHttpClient;

import static org.mockito.Mockito.*;
import java.util.Date;

import static com.wootric.androidsdk.TestUtils.*;

/**
 * Created by maciejwitowski on 4/13/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SurveyValidatorTest {

    private SurveyValidator surveyValidator;

    @Before
    public void setup() {
        surveyValidator = new SurveyValidator(testActivity(),
                TestUtils.ACCOUNT_TOKEN, TestUtils.END_USER_EMAIL);
    }

    @Test
    public void doesNotNeedSurveyIfSurveyedRecently() throws Exception {
        sharedPrefs().setLastSurveyed(new Date().getTime() - 89*1000*60*60*24);

        SurveyValidator.OnSurveyValidatedListener listener = mock(SurveyValidator.OnSurveyValidatedListener.class);

        surveyValidator.setSurveyImmediately(true);
        surveyValidator.validate();

        verify(listener, never()).onSurveyValidated();
    }
}
