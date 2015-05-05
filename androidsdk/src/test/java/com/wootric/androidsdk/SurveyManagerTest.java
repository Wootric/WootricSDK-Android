package com.wootric.androidsdk;

import android.app.Activity;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.utils.ConnectionUtils;
import com.wootric.androidsdk.utils.PreferencesUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.ref.WeakReference;

import static com.wootric.androidsdk.TestUtils.END_USER_EMAIL;
import static com.wootric.androidsdk.TestUtils.ORIGIN_URL;
import static com.wootric.androidsdk.TestUtils.testActivity;
import static com.wootric.androidsdk.TestUtils.testUser;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by maciejwitowski on 4/11/15.
*/
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, emulateSdk = 21)
public class SurveyManagerTest {

    private SurveyManager surveyManager;

    @Mock SurveyValidator surveyValidator;
    @Mock ConnectionUtils connectionUtils;
    @Mock PreferencesUtils preferencesUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        surveyManager = new SurveyManager(new WeakReference<Activity>(testActivity()),
                testUser(),
                new EndUser(1, END_USER_EMAIL),
                surveyValidator,
                ORIGIN_URL,
                preferencesUtils,
                connectionUtils);
    }

    @Test
    public void survey_doesNotUpdateLastSeen_ifRecentlyUpdated() throws Exception {
        when(preferencesUtils.wasRecentlySeen()).thenReturn(true);

        surveyManager.survey();

        verify(preferencesUtils, never()).setLastSeen(Mockito.anyLong());
    }

    @Test
    public void survey_updatesLastSeen_ifNotRecentlyUpdated() throws Exception {
        when(preferencesUtils.wasRecentlySeen()).thenReturn(false);

        surveyManager.survey();

        verify(preferencesUtils, times(1)).setLastSeen(Mockito.anyLong());
    }

    @Test
    public void survey_startsValidation() throws Exception {
        surveyManager.survey();

        verify(surveyValidator, times(1)).validate();
    }
}