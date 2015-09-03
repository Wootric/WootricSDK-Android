package com.wootric.androidsdk;

import android.app.Activity;

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

import java.lang.ref.WeakReference;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.ORIGIN_URL;
import static com.wootric.androidsdk.TestUtils.testActivity;
import static com.wootric.androidsdk.TestUtils.testUser;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Created by maciejwitowski on 4/11/15.
*/
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SurveyManagerOLDTest {

    private SurveyManager_OLD surveyManagerOLD;

    @Mock
    SurveyValidator_OLD surveyValidatorOLD;
    @Mock ConnectionUtils connectionUtils;
    @Mock PreferencesUtils preferencesUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        surveyManagerOLD = new SurveyManager_OLD(new WeakReference<Activity>(testActivity()),
                testUser(),
                new EndUser(1, END_USER_EMAIL),
                surveyValidatorOLD,
                ORIGIN_URL,
                preferencesUtils,
                connectionUtils);
    }

    @Test
    public void survey_updatesLastSeen() throws Exception {
        surveyManagerOLD.survey();

        verify(preferencesUtils, times(1)).touchLastSeen();
    }

    @Test
    public void survey_startsValidation() throws Exception {
        surveyManagerOLD.survey();

        verify(surveyValidatorOLD, times(1)).validate();
    }
}